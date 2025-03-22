package com.daniilmiskevich.labs.space.controller;

import java.net.URI;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
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
import com.daniilmiskevich.labs.space.controller.dto.SpaceRequestDto;
import com.daniilmiskevich.labs.space.controller.dto.SpaceRequestDto.Name;
import com.daniilmiskevich.labs.space.controller.dto.SpaceResponseDto;
import com.daniilmiskevich.labs.space.service.SpaceService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/spaces")
@Tag(name = "Spaces")
public class SpaceController {

    private final SpaceService service;

    public SpaceController(SpaceService service) {
        this.service = service;
    }

    @Operation(
        summary = "search spaces by name",
        description = "Returns a list of spaces matching the provided name pattern")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved spaces"),
    })
    @GetMapping("")
    public List<SpaceResponseDto> searchByName(
        @Name(message = Name.PATTERN_MESSAGE, doAcceptPatterns = true)
        @Parameter(description = "Name pattern to search for spaces", example = "example*space")
        @RequestParam(name = "name", required = false) String namePattern) {
        return service.match(namePattern)
            .stream()
            .map(SpaceResponseDto::new)
            .toList();
    }

    @Operation(
        summary = "get space by ID or name",
        description = "Returns a space by its ID or name")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved space by ID"),
        @ApiResponse(responseCode = "302", description = "Successfully retrieved space by name"),
    })
    @GetMapping("/{idOrName}")
    public ResponseEntity<SpaceResponseDto> getByIdOrName(
        @Parameter(description = "ID or name of the space", example = "123 or 'example-space'")
        @PathVariable String idOrName) {
        try {
            var id = Long.decode(idOrName);
            return ResponseEntity.ok(getById(id));
        } catch (NumberFormatException e) {
            return getByName(idOrName);
        }
    }

    private SpaceResponseDto getById(@NotNull Long id) {
        var spaceById = service.findById(id)
            .orElseThrow(EntityNotFoundException::new);

        return new SpaceResponseDto(spaceById);
    }

    private ResponseEntity<SpaceResponseDto> getByName(@Name String name) {
        var spaceByName = service.findByName(name)
            .orElseThrow(EntityNotFoundException::new);

        return ResponseEntity.status(HttpStatus.FOUND)
            .location(URI.create(spaceByName.getId().toString()))
            .build();
    }

    @Operation(
        summary = "create a new space",
        description = "Creates a new space with the provided details"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created space"),
    })
    @PostMapping("")
    public SpaceResponseDto create(
        @Valid @RequestBody SpaceRequestDto space) {
        return new SpaceResponseDto(service.create(space.toSpace(null)));
    }

    @Operation(
        summary = "update a space by ID",
        description = "Updates an existing space with the provided details"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated space"),
    })
    @PatchMapping("/{id}")
    public SpaceResponseDto updateById(
        @Parameter(description = "ID of the space", example = "123")
        @NotNull @PathVariable Long id,
        @Valid @RequestBody SpaceRequestDto space) {
        return new SpaceResponseDto(service.update(space.toSpace(id)));
    }

    @Operation(
        summary = "delete a space by ID",
        description = "Deletes a space by its ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted space"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
        @Parameter(description = "ID of the space", example = "123")
        @NotNull @PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
