package com.daniilmiskevich.labs.space.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.daniilmiskevich.labs.space.model.Space;
import com.daniilmiskevich.labs.space.service.SpaceService;

@RestController
@RequestMapping("/api/spaces")
public class SpaceController {

    public SpaceController(SpaceService service) {
        this.service = service;
    }

    private final SpaceService service;

    @GetMapping("")
    public List<Space> searchByName(@RequestParam(required = false) Optional<String> query) {
        if (query.isEmpty()) {
            return service.findAll();
        } else {
            try {
                return service.matchByName(query.get());
            } catch (SpaceService.InvalidQueryException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
        }
    }

    @GetMapping("/{name}")
    public Optional<Space> getByName(@PathVariable String name) {
        try {
            return service.findByName(name);
        } catch (SpaceService.InvalidNameException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

}
