package com.daniilmiskevich.labs.space.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
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
@Tag(name = "Sparks")
public class SparkController {

    private final SparkService service;

    public SparkController(SparkService service) {
        this.service = service;
    }

    @Operation(
        summary = "search sparks by name or spectre pattern",
        description = "Returns a list of sparks matching the provided name and spectre pattern"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved sparks"),
    })
    @GetMapping("")
    public List<SparkResponseDto> search(
        @Parameter(description = "Name pattern to search for sparks", example = "Example *")
        @RequestParam(name = "name", required = false) String namePattern,
        @Parameter(description = "Spectres to search for in sparks", example = "foo,bar,foo-bar")
        @SpectreName(message = SpectreName.PATTERN_MESSAGE, doAcceptPatterns = true)
        @RequestParam(name = "spectres", required = false) String spectrePattern) {
        return service.match(namePattern, spectrePattern)
            .stream()
            .map(SparkResponseDto::new)
            .toList();
    }

    @Operation(
        summary = "get spark by ID",
        description = "Returns a spark by its ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved spark"),
    })
    @GetMapping("/{id}")
    public SparkResponseDto getById(
        @Parameter(description = "ID of the spark", example = "123")
        @NotNull @PathVariable Long id) {
        var sparkById = service.findById(id)
            .orElseThrow(EntityNotFoundException::new);

        return new SparkResponseDto(sparkById);
    }

    @Operation(
        summary = "create a new spark",
        description = "Creates a new spark with the provided details"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created spark"),
    })
    @PostMapping("")
    public SparkResponseDto create(
        @Parameter(description = "ID of the space to which the spark belongs", example = "123")
        @NotNull @RequestParam(name = "space_id") Long spaceId,
        @Valid @RequestBody SparkRequestDto spark) {
        return new SparkResponseDto(service.create(spaceId, spark.toSpark(null)));
    }

    @Operation(
        summary = "create a lot of new sparks at once",
        description = "Creates a lot of new sparks at once with the provided details"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created all the sparks"),
    })
    @PostMapping("/bulk")
    public List<SparkResponseDto> createBulk(
        @Parameter(description = "ID of the space to which the spark belongs", example = "123")
        @NotNull @RequestParam(name = "space_id") Long spaceId,
        @RequestBody List<@Valid SparkRequestDto> sparks) {
        return service
            .createAll(
                spaceId,
                sparks
                    .stream()
                    .map(sparkRequestDto -> sparkRequestDto.toSpark(null))
                    .toList())
            .stream()
            .map(SparkResponseDto::new)
            .toList();
    }

    @Operation(
        summary = "update a spark by ID",
        description = "Updates an existing spark with the provided details"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated spark"),
    })
    @PatchMapping("/{id}")
    public SparkResponseDto updateById(
        @Parameter(description = "ID of the spark", example = "123")
        @NotNull @PathVariable Long id,
        @Valid @RequestBody SparkRequestDto spark) {
        return new SparkResponseDto(service.update(spark.toSpark(id)));
    }

    @Operation(
        summary = "delete a spark by ID",
        description = "Deletes a spark by its ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted spark"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
        @Parameter(description = "ID of the spark", example = "123")
        @NotNull @PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
