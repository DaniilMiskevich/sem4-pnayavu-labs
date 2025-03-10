package com.daniilmiskevich.labs.space.controller.dto;

import java.util.Set;
import java.util.stream.Collectors;
import com.daniilmiskevich.labs.space.model.Spark;
import com.daniilmiskevich.labs.space.model.Spectre;

public record SparkResponseDto(Long id, String name, Long spaceId, Set<String> spectreNames) {
    public SparkResponseDto(Spark spark) {
        this(
            spark.getId(),
            spark.getName(),
            spark.getSpace().getId(),
            spark.getSpectres()
                .stream()
                .map(Spectre::getName)
                .collect(Collectors.toSet()));
    }
}
