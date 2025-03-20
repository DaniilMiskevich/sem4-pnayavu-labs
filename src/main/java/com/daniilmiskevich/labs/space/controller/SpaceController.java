package com.daniilmiskevich.labs.space.controller;

import java.io.IOException;
import java.util.List;

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

import com.daniilmiskevich.labs.space.controller.dto.SpaceRequestDto;
import com.daniilmiskevich.labs.space.controller.dto.SpaceRequestDto.Name;
import com.daniilmiskevich.labs.space.controller.dto.SpaceResponseDto;
import com.daniilmiskevich.labs.space.service.SpaceService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/spaces")
public class SpaceController {

    private final SpaceService service;

    public SpaceController(SpaceService service) {
        this.service = service;
    }

    @GetMapping("")
    public List<SpaceResponseDto> searchByName(
        @Name(message = Name.PATTERN_MESSAGE, doAcceptPatterns = true)

        @RequestParam(name = "name", required = false) String namePattern) {
        return service.match(namePattern)
            .stream()
            .map(SpaceResponseDto::new)
            .toList();
    }

    @GetMapping("/{idOrName}")
    public SpaceResponseDto getByIdOrName(
        @PathVariable String idOrName,
        HttpServletResponse response) {
        try {
            return getById(Long.decode(idOrName));
        } catch (NumberFormatException e) {
            getByName(idOrName, response);
            return null;
        }
    }

    private SpaceResponseDto getById(@NotNull Long id) {
        var spaceById = service.findById(id)
            .orElseThrow(() -> new EntityNotFoundException());

        return new SpaceResponseDto(spaceById);
    }

    private void getByName(
        @Name String name,
        HttpServletResponse response) {
        var spaceByName = service.findByName(name)
            .orElseThrow(() -> new EntityNotFoundException());

        try {
            response.sendRedirect(spaceByName.getId().toString());
        } catch (IOException e) {
            throw new RuntimeException("Redirect failed!", e);
        }
    }

    @PostMapping("")
    public SpaceResponseDto create(@Valid @RequestBody SpaceRequestDto space) {
        return new SpaceResponseDto(service.create(space.toSpace(null)));
    }

    @PatchMapping("/{id}")
    public SpaceResponseDto updateById(
        @NotNull @PathVariable Long id,
        @Valid @RequestBody SpaceRequestDto space) {
        return new SpaceResponseDto(service.update(space.toSpace(id)));
    }

    @DeleteMapping("/{id}")
    public void deleteById(@NotNull @PathVariable Long id) {
        service.deleteById(id);
    }

}
