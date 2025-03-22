package com.daniilmiskevich.labs.dev.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.daniilmiskevich.labs.dev.service.LogService;
import com.daniilmiskevich.labs.exceptions.exception.InvalidRangeException;
import jakarta.validation.ValidationException;

@RestController
@RequestMapping("/dev/logs")
@Hidden
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
        var start = LocalDateTime.MIN;
        var end = LocalDateTime.MAX;
        if (startString != null) {
            try {
                start = LocalDateTime.parse(startString, START_END_FORMATTER);
            } catch (DateTimeParseException e) {
                throw new ValidationException(e.getMessage());
            }
        }
        if (endString != null) {
            try {
                end = LocalDateTime.parse(endString, START_END_FORMATTER);
            } catch (DateTimeParseException e) {
                throw new ValidationException(e.getMessage());
            }
        }

        if (end.isBefore(start)) {
            throw new InvalidRangeException(start, end);
        }

        return String.join("\n", service.filtered(start, end));
    }

}

