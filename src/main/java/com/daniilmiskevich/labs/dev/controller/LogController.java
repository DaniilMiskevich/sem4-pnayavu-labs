package com.daniilmiskevich.labs.dev.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.daniilmiskevich.labs.dev.registry.AsyncLogFileRegistry;
import jdk.jfr.Frequency;
import org.springframework.core.io.Resource;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.core.io.UrlResource;
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
    public Long asyncLogFileCreate(
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

        return registry.add(service.asyncLogFile(start, end));
    }

    @GetMapping(value = "/async/{id}", produces = "text/plain")
    public ResponseEntity<Resource> asyncLogFileGet(@PathVariable Long id) {
        var status = registry.getStatus(id).orElseThrow();

        return switch (status) {
            case RUNNING -> ResponseEntity.accepted().build();
            case COMPLETED -> {
                var result = registry.getResultPath(id).orElseThrow();
                try {
                    yield ResponseEntity.ok(new UrlResource(result.toUri()));
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }
            case FAILED -> throw registry.getFailure(id).orElseThrow();
        };
    }

    @GetMapping(value = "/async/{id}/status", produces = "text/plain")
    public String asyncLogFileStatus(@PathVariable Long id) {
        var status = registry.getStatus(id).orElseThrow();

        return switch (status) {
            case RUNNING -> "running";
            case COMPLETED -> "completed";
            case FAILED -> "failed";
        };
    }

}

