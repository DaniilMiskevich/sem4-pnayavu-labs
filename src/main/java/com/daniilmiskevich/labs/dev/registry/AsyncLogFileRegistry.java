package com.daniilmiskevich.labs.dev.registry;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class AsyncLogFileRegistry {

    private final ConcurrentHashMap<Long, CompletableFuture<Path>> tasks;
    private final AtomicLong taskIdCounter = new AtomicLong(0L);

    public AsyncLogFileRegistry() {
        tasks = new ConcurrentHashMap<>();
    }

    public Long add(CompletableFuture<Path> future) {
        var newId = taskIdCounter.getAndIncrement();
        tasks.put(newId, future);
        return newId;
    }

    public Optional<AsyncLogFileStatus> getStatus(Long id) {
        return Optional.ofNullable(tasks.get(id)).map(task -> switch (task.state()) {
            case RUNNING -> AsyncLogFileStatus.RUNNING;
            case SUCCESS -> AsyncLogFileStatus.COMPLETED;
            default -> AsyncLogFileStatus.FAILED;
        });
    }

    public Optional<Path> getResultPath(Long id) {
        try {
            return Optional.ofNullable(tasks.get(id)).map(CompletableFuture::resultNow);
        } catch (IllegalStateException e) {
            return Optional.empty();
        }
    }

    public Optional<RuntimeException> getFailure(Long id) {
        try {
            return Optional.ofNullable(tasks.get(id))
                .map(CompletableFuture::exceptionNow)
                .map(e -> e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException(e));
        } catch (IllegalStateException e) {
            return Optional.empty();
        }
    }

    public enum AsyncLogFileStatus { RUNNING, COMPLETED, FAILED }

}
