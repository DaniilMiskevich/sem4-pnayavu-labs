package com.daniilmiskevich.labs.space.controller.dto;

import java.util.List;
import java.util.Optional;

import com.daniilmiskevich.labs.space.model.Space;

public record SpaceResponseDto(Long id, String name, List<SparkResponseDto> sparks) {
    public SpaceResponseDto(Space space) {
        this(
            space.getId(),
            space.getName(),
            Optional.ofNullable(space.getSparks())
                .orElse(List.of())
                .stream()
                .map(SparkResponseDto::new)
                .toList());
    }
}
