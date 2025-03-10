package com.daniilmiskevich.labs.space.controller.dto;

import com.daniilmiskevich.labs.space.model.Spectre;

public record SpectreResponseDto(String name) {
    public SpectreResponseDto(Spectre spectre) {
        this(spectre.getName());
    }
}
