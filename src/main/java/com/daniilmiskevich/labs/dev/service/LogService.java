package com.daniilmiskevich.labs.dev.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LogService {

    private final String logPath;
    private final String logName;

    public LogService(
        @Value("${logging.file.path:null}") String logPath,
        @Value("${logging.file.name}") String logName) {
        this.logPath = logPath;
        this.logName = logName;
    }

    public List<String> filtered(LocalDateTime start, LocalDateTime end) {
        var log = new File(logPath, logName);
        if (!log.exists()) {
            return List.of();
        }

        try {
            return Files.readAllLines(log.toPath())
                .stream()
                .filter(line -> {
                    try {
                        var timestampString = line.split(" ", 2)[0];
                        var timestamp =
                            LocalDateTime.parse(timestampString, DateTimeFormatter.ISO_DATE_TIME);
                        return timestamp.isAfter(start) && timestamp.isBefore(end);
                    } catch (DateTimeParseException | IndexOutOfBoundsException e) {
                        return false;
                    }
                })
                .toList();
        } catch (IOException e) {
            return List.of();
        }
    }

}
