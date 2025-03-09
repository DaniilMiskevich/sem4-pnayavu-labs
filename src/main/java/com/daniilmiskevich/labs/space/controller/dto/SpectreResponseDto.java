package com.daniilmiskevich.labs.space.controller.dto;

import java.util.List;
import java.util.Optional;

import com.daniilmiskevich.labs.space.model.Spectre;

public record SpectreResponseDto(String name, List<SparkResponseDto> sparksWithin) {
    public SpectreResponseDto(Spectre spectre) {
        this(
            spectre.getName(),
            Optional.ofNullable(spectre.getSparksWithin())
                .orElse(List.of())
                .stream()
                .map(SparkResponseDto::new)
                .toList());
    }
}
