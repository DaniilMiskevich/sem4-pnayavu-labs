package com.daniilmiskevich.labs.space.controller.dto;

import java.util.Set;
import java.util.stream.Collectors;
import com.daniilmiskevich.labs.space.model.Spark;
import com.daniilmiskevich.labs.space.model.Spectre;
import com.fasterxml.jackson.annotation.JsonProperty;

public record SparkRequestDto(
    String name,
    @JsonProperty("spectre_names") Set<String> spectreNames) {

    public Spark toSpark(Long id) {
        return new Spark(
            id,
            name,
            spectreNames
                .stream()
                .map(Spectre::new)
                .collect(Collectors.toSet()));

    }
}
