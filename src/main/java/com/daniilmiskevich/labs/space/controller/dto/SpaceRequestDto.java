package com.daniilmiskevich.labs.space.controller.dto;

import com.daniilmiskevich.labs.space.model.Space;

public record SpaceRequestDto(String name) {
    public Space toSpace(Long id) {
        return new Space(id, name);
    }
}
