package com.daniilmiskevich.labs.dev.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import com.daniilmiskevich.labs.dev.registry.AsyncLogFileRegistry;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    private final AsyncLogFileRegistry registry;

    public LogController(LogService service, AsyncLogFileRegistry registry) {
        this.service = service;
        this.registry = registry;
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

    @PostMapping(value = "/async")
    public Long asyncLogFileCreate() {
        return registry.addAsyncLogFile(service.asyncLogFile());
    }

    @GetMapping(value = "/async/{id}", produces = "text/plain")
    public ResponseEntity<String> asyncLogFileGet(@PathVariable Long id) {
        return ResponseEntity.ok(
            registry.getAsyncLogFile(id)
                .map(filePath -> {
                    try {
                        return String.join("\n", Files.readAllLines(filePath));
                    } catch (IOException e) {
                        return "\n";
                    }
                })
                .or(() -> registry.getFailure(id).map(Throwable::toString))
                .orElseThrow());
    }

    @GetMapping(value = "/async/{id}/status", produces = "text/plain")
    public String asyncLogFileStatus(@PathVariable Long id) {
        var status = registry.getAsyncLogFileStatus(id).orElseThrow();

        return switch (status) {
            case RUNNING -> "running";
            case COMPLETED -> "completed";
            case FAILED -> "failed";
        };
    }

}

