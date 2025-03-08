package com.daniilmiskevich.labs.space.controller;

import java.io.IOException;
import java.util.Collections;
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

import com.daniilmiskevich.labs.space.model.Space;
import com.daniilmiskevich.labs.space.service.SpaceService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/spaces")
public class SpaceController {

    private record SpaceResponseDto(Long id, String name) {
        public SpaceResponseDto(Space space) {
            this(space.getId(), space.getName());
        }
    }

    private record SpaceRequestDto(String name) {
        public Space toSpace(Long id) {
            return new Space(id, name);
        }
    }

    private final SpaceService service;

    public SpaceController(SpaceService service) {
        this.service = service;
    }

    @GetMapping("")
    public List<SpaceResponseDto> searchByName(@RequestParam(required = false) String pattern) {
        return (switch (pattern) {
            case null -> service.findAll();
            case String p when !isValidNamePattern(p) -> Collections.<Space>emptyList();
            case String p -> service.matchByName(p);
        }).stream().map(SpaceResponseDto::new).toList();
    }

    @GetMapping("/{idOrName}")
    public SpaceResponseDto getByIdOrName(@PathVariable String idOrName, HttpServletResponse response) {
        try {
            return getById(Long.decode(idOrName));
        } catch (NumberFormatException e) {
            getByName(idOrName, response);
            return null;
        }
    }

    private SpaceResponseDto getById(Long id) {
        var optionalSpaceById = service.findById(id);
        if (optionalSpaceById.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        var spaceById = optionalSpaceById.get();

        return new SpaceResponseDto(spaceById);
    }

    private void getByName(String name, HttpServletResponse response) {
        if (!isValidName(name)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        var spaceByName = service.findByName(name);
        if (spaceByName.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        try {
            response.sendRedirect(spaceByName.get().getId().toString());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("")
    public SpaceResponseDto create(@RequestBody SpaceRequestDto space) {
        if (!isValidName(space.name())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return new SpaceResponseDto(service.create(space.toSpace(null)));
    }

    @PatchMapping("/{id}")
    public SpaceResponseDto updateById(@PathVariable Long id, @RequestBody SpaceRequestDto space) {
        if (!isValidName(space.name())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return new SpaceResponseDto(service.update(space.toSpace(id)));
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        service.deleteById(id);
    }

    private boolean isValidName(String name) {
        return name.matches("^[A-Za-z-_][A-Za-z-_0-9]*$");
    }

    private boolean isValidNamePattern(String pattern) {
        return isValidName(pattern.replace("*", ""));
    }

}
