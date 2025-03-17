package com.daniilmiskevich.labs.space.controller.dto;

import java.util.Set;
import java.util.stream.Collectors;

import com.daniilmiskevich.labs.space.model.Spark;
import com.daniilmiskevich.labs.space.model.Spectre;
import com.fasterxml.jackson.annotation.JsonProperty;

public record SparkResponseDto(
    Long id,
    String name,
    @JsonProperty("space_id") Long spaceId,
    @JsonProperty("spectre_names") Set<String> spectreNames) {
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
