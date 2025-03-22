package com.daniilmiskevich.labs.space.controller.dto;

import java.util.List;

import com.daniilmiskevich.labs.space.model.Space;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response DTO for a space")
public record SpaceResponseDto(
    @Schema(description = "Unique sequential ID of the space")
    Long id,
    @Schema(description = "Name of the space")
    String name,
    @Schema(description = "List of sparks published in the space")
    List<SparkResponseDto> sparks) {
    public SpaceResponseDto(Space space) {
        this(
            space.getId(),
            space.getName(),
            space.getSparks()
                .stream()
                .map(SparkResponseDto::new)
                .toList());
    }
}
