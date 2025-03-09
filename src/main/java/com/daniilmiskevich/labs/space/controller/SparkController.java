package com.daniilmiskevich.labs.space.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.daniilmiskevich.labs.space.controller.dto.SparkRequestDto;
import com.daniilmiskevich.labs.space.controller.dto.SparkResponseDto;
import com.daniilmiskevich.labs.space.service.SparkService;

@RestController
@RequestMapping("/api/sparks")
public class SparkController {

    private final SparkService service;

    public SparkController(SparkService service) {
        this.service = service;
    }

    @GetMapping("")
    public List<SparkResponseDto> searchByName(@RequestParam(required = false) String pattern) {
        return (pattern == null
                ? service.findAll()
                : service.matchByName(pattern))
                .stream()
                .map(SparkResponseDto::new)
                .toList();
    }

    @GetMapping("/{id}")
    private SparkResponseDto getById(@PathVariable Long id) {
        var optionalSparkById = service.findById(id);
        if (optionalSparkById.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        var sparkById = optionalSparkById.get();

        return new SparkResponseDto(sparkById);
    }

    @PostMapping("")
    public SparkResponseDto create(
            @RequestParam(name = "space_id") Long spaceId,
            @RequestBody SparkRequestDto spark) {
        return new SparkResponseDto(service.create(spaceId, spark.toSpark(null)));
    }

    @PatchMapping("/{id}")
    public SparkResponseDto updateById(@PathVariable Long id, @RequestBody SparkRequestDto spark) {
        return new SparkResponseDto(service.update(spark.toSpark(id)));
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        service.deleteById(id);
    }

}
