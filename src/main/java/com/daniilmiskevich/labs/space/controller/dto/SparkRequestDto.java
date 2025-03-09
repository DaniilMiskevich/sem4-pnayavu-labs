package com.daniilmiskevich.labs.space.controller.dto;

import com.daniilmiskevich.labs.space.model.Spark;

public record SparkRequestDto(String name) {
    public Spark toSpark(Long id) {
        return new Spark(id, name);
    }
}
