package com.daniilmiskevich.labs.space.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.daniilmiskevich.labs.space.model.Space;
import com.daniilmiskevich.labs.space.repository.SpaceRepository;

@Service
public class SpaceService {

    private final SpaceRepository repository;

    public SpaceService(SpaceRepository repository) {
        this.repository = repository;
    }

    public List<Space> findAll() {
        return repository.findAll();
    }

    public Optional<Space> findByName(String name) {
        return repository.findByName(name);
    }

    public List<Space> matchByName(String pattern) {
        var regexp = pattern
                .replace("*", ".*");

        return repository.matchByName(regexp);
    }

    public void create(Space space) {
        repository.create(space);
    }

    public void updateByName(String name, Space space) {
        repository.updateByName(name, space);
    }

    public void deleteByName(String name) {
        repository.deleteByName(name);
    }

}
