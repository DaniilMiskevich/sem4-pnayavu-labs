package com.daniilmiskevich.labs.space.controller;

import java.util.List;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.daniilmiskevich.labs.space.controller.dto.SparkRequestDto;
import com.daniilmiskevich.labs.space.controller.dto.SparkRequestDto.SpectreName;
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
    public List<SparkResponseDto> search(
        @RequestParam(name = "name", required = false) String namePattern,

        @SpectreName(message = SpectreName.DEFAULT_MESSAGE, doAcceptPatterns = true)

        @RequestParam(name = "spectres", required = false) String spectrePattern) {
        return service.match(namePattern, spectrePattern)
            .stream()
            .map(SparkResponseDto::new)
            .toList();
    }

    @GetMapping("/{id}")
    public SparkResponseDto getById(@NotNull @PathVariable Long id) {
        var sparkById = service.findById(id)
            .orElseThrow(EntityNotFoundException::new);

        return new SparkResponseDto(sparkById);
    }

    @PostMapping("")
    public SparkResponseDto create(
        @NotNull @RequestParam(name = "space_id") Long spaceId,
        @Valid @RequestBody SparkRequestDto spark) {
        return new SparkResponseDto(service.create(spaceId, spark.toSpark(null)));
    }

    @PatchMapping("/{id}")
    public SparkResponseDto updateById(
        @NotNull @PathVariable Long id,
        @Valid @RequestBody SparkRequestDto spark) {
        return new SparkResponseDto(service.update(spark.toSpark(id)));
    }

    @DeleteMapping("/{id}")
    public void deleteById(@NotNull @PathVariable Long id) {
        service.deleteById(id);
    }

}
