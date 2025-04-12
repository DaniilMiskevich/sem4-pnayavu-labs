package com.daniilmiskevich.labs.dev.registry;

import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AsyncLogFileRegistry {

    private final ConcurrentHashMap<Long, CompletableFuture<Path>> tasks;
    private Long taskIdCounter = 0L;

    public AsyncLogFileRegistry() {
        tasks = new ConcurrentHashMap<>();
    }

    public Long addAsyncLogFile(CompletableFuture<Path> future) {
        tasks.put(taskIdCounter, future);
        return taskIdCounter++;
    }

    public Optional<Path> getAsyncLogFile(Long id) {
        try {
            return Optional.ofNullable(tasks.get(id)).map(CompletableFuture::resultNow);
        } catch (IllegalStateException e) {
            return Optional.empty();
        }
    }

    public Optional<Throwable> getFailure(Long id) {
        try {
            return Optional.ofNullable(tasks.get(id)).map(CompletableFuture::exceptionNow);
        } catch (IllegalStateException e) {
            return Optional.empty();
        }
    }

    public Optional<AsyncLogFileStatus> getAsyncLogFileStatus(Long id) {
        return Optional.ofNullable(tasks.get(id)).map(task -> switch (task.state()) {
            case RUNNING -> AsyncLogFileStatus.RUNNING;
            case SUCCESS -> AsyncLogFileStatus.COMPLETED;
            default -> AsyncLogFileStatus.FAILED;
        });
    }

    public enum AsyncLogFileStatus { RUNNING, COMPLETED, FAILED }

}
