package com.daniilmiskevich.labs.space.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

import com.daniilmiskevich.labs.space.model.Space;
import com.daniilmiskevich.labs.space.repository.SpaceRepository;

import jakarta.annotation.PostConstruct;

@Repository
public class MemSpaceRepositoryImpl implements SpaceRepository {

    private final List<Space> spaces = new ArrayList<>();

    public List<Space> findAll() {
        return spaces;
    }

    public Optional<Space> findByName(String name) {
        return spaces.stream()
                .filter(space -> space.name().equals(name))
                .findFirst();
    }

    public List<Space> matchByName(String regexp) {
        return spaces.stream()
                .filter(space -> space.name().matches(regexp))
                .toList();
    }

    @PostConstruct
    private void init() {
        spaces.add(new Space("foo"));
        spaces.add(new Space("bar"));
        spaces.add(new Space("foo-bar"));
    }

}
