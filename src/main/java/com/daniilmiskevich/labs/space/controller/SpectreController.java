package com.daniilmiskevich.labs.space.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.daniilmiskevich.labs.space.controller.dto.SparkResponseDto;
import com.daniilmiskevich.labs.space.controller.dto.SpectreResponseDto;
import com.daniilmiskevich.labs.space.service.SpectreService;

@RestController
@RequestMapping("/api/spectres")
public class SpectreController {

    private final SpectreService service;

    public SpectreController(SpectreService service) {
        this.service = service;
    }

    @GetMapping("/{name}")
    private List<SparkResponseDto> getSparksWithinByName(@PathVariable String name) {
        var optionalSpectre = service.findByName(name);
        if (optionalSpectre.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        var spectreById = optionalSpectre.get();

        return spectreById.getSparksWithin()
                .stream()
                .map(SparkResponseDto::new)
                .toList();
    }

    @PatchMapping("/{name}")
    public SpectreResponseDto addSparkByName(
            @PathVariable String name,
            @RequestParam(name = "spark_id") Long sparkId) {
        return new SpectreResponseDto(service.addSparkByName(name, sparkId));
    }

    @DeleteMapping("/{name}")
    public void deleteById(
            @PathVariable String name,
            @RequestParam(name = "spark_id") Long sparkId) {
        service.deleteSparkByName(name, sparkId);
    }

}
