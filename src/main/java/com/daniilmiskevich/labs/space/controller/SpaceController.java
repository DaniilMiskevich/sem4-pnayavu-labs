package com.daniilmiskevich.labs.space.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.daniilmiskevich.labs.space.model.Space;
import com.daniilmiskevich.labs.space.service.SpaceService;

@RestController
@RequestMapping("/api/spaces")
public class SpaceController {

    private final SpaceService service;

    public SpaceController(SpaceService service) {
        this.service = service;
    }

    @GetMapping("")
    public List<Space> searchByName(@RequestParam(required = false) String pattern) {
        return switch (pattern) {
            case null -> service.findAll();
            case String p when !isValidNamePattern(p) -> Collections.emptyList();
            case String p -> service.matchByName(p);
        };
    }

    @GetMapping("/{name}")
    public Optional<Space> getByName(@PathVariable String name) {
        if (!isValidName(name)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return service.findByName(name);
    }

    @PostMapping("")
    public void create(@RequestBody Space space) {
        if (!isValidName(space.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        service.create(space);
    }

    @PutMapping("/{name}")
    public void update(@PathVariable String name, @RequestBody Space space) {
        if (!isValidName(name)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        service.updateByName(name, space);
    }

    @DeleteMapping("/{name}")
    public void deleteByName(@PathVariable String name) {
        if (!isValidName(name)) {
            return;
        }

        service.deleteByName(name);
    }

    private boolean isValidName(String name) {
        return !name.matches("[^A-Za-z0-9-_]");
    }

    private boolean isValidNamePattern(String pattern) {
        return isValidName(pattern.replace("*", ""));
    }

}
