package com.daniilmiskevich.labs.dev.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import com.daniilmiskevich.labs.dev.service.LogService;

@RestController
@RequestMapping("/dev/logs")
public class LogController {

    private static final DateTimeFormatter START_END_FORMATTER =
        DateTimeFormatter.ofPattern("dd.MM.yyyy_HH:mm");


    private final LogService service;

    public LogController(LogService service) {
        this.service = service;
    }

    @GetMapping(value = "", produces = "text/plain")
    public String filter(
        @RequestParam(name = "start", required = false) String startString,
        @RequestParam(name = "end", required = false) String endString) {
        try {
            var start =
                startString != null
                    ? LocalDateTime.parse(startString, START_END_FORMATTER)
                    : LocalDateTime.of(0, 1, 1, 0, 0);
            var end =
                endString != null
                    ? LocalDateTime.parse(endString, START_END_FORMATTER)
                    : LocalDateTime.now();

            return String.join("\n", service.filtered(start, end));
        } catch (DateTimeParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

}

