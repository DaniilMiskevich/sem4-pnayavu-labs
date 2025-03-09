package com.daniilmiskevich.labs.space.controller.dto;

import com.daniilmiskevich.labs.space.model.Spark;

public record SparkResponseDto(Long id, String name, Long spaceId) {
    public SparkResponseDto(Spark spark) {
        this(spark.getId(), spark.getName(), spark.getSpace().getId());
    }
}
