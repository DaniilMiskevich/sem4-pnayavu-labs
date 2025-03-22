package com.daniilmiskevich.labs.space.controller.dto;

import java.util.Set;
import java.util.stream.Collectors;

import com.daniilmiskevich.labs.space.model.Spark;
import com.daniilmiskevich.labs.space.model.Spectre;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response DTO for a spark")
public record SparkResponseDto(
    @Schema(description = "Unique sequential ID of the spark")
    Long id,
    @Schema(description = "Name of the spark")
    String name,
    @Schema(description = "ID of the space to which the spark belongs")
    @JsonProperty("space_id") Long spaceId,
    @Schema(description = "Set of spectre names associated with the spark")
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
