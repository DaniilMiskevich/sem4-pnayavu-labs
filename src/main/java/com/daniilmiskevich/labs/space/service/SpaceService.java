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
        if (!isValidName(name)) {
            throw new InvalidNameException(name);
        }

        return repository.findByName(name);
    }

    public List<Space> matchByName(String query) {
        if (!isValidQuery(query)) {
            throw new InvalidQueryException(query);
        }

        var regexp = query
                .replaceAll("[^A-Za-z0-9-_*]+", "")
                .replace("*", ".*");
        System.out.println(regexp);

        return repository.matchByName(regexp);
    }

    private boolean isValidName(String name) {
        return !name.matches("[^A-Za-z0-9-_]");
    }

    private boolean isValidQuery(String query) {
        return isValidName(query.replace("*", ""));
    }

    public static class InvalidNameException extends RuntimeException {
        public InvalidNameException(String name) {
            super(String.format("Invalid name: '%s'.", name));
        }
    }

    public static class InvalidQueryException extends RuntimeException {
        public InvalidQueryException(String query) {
            super(String.format("Invalid query: '%s'.", query));
        }
    }

}
