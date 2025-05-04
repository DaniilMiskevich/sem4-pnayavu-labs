package com.daniilmiskevich.labs.dev.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
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

    @Async
    public CompletableFuture<Path> asyncLogFile(LocalDateTime start, LocalDateTime end) {
        var log = new File(logPath, logName);
        if (!log.exists()) {
            return CompletableFuture.failedFuture(new FileNotFoundException());
        }

        try {
            var filteredLog = Files.createTempFile(
                logName,
                String.format("%s_to_%s",
                    start != null ? start.format(DateTimeFormatter.ISO_DATE) : "any",
                    end != null ? end.format(DateTimeFormatter.ISO_DATE) : "any"))
                .toFile();
            // filteredLog.deleteOnExit();

            var writer = new FileWriter(filteredLog);
            Files.readAllLines(log.toPath())
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
                .forEach(line -> {
                    try {
                        writer.write(line);
                        writer.write("\n");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            Thread.sleep(5 * 1000);
            writer.close();
            return CompletableFuture.completedFuture(filteredLog.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

}
