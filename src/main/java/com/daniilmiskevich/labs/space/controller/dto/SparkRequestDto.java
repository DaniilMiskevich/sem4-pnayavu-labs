package com.daniilmiskevich.labs.space.controller.dto;

import java.util.Set;
import java.util.stream.Collectors;
import com.daniilmiskevich.labs.space.model.Spark;
import com.daniilmiskevich.labs.space.model.Spectre;

public record SparkRequestDto(String name, Set<String> spectreNames) {
    public Spark toSpark(Long id) {
        return new Spark(
            id,
            name,
            spectreNames != null
                ? spectreNames
                    .stream()
                    .map(Spectre::new)
                    .collect(Collectors.toSet())
                : null);

    }
}
