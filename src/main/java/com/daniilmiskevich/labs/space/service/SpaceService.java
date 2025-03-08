package com.daniilmiskevich.labs.space.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.daniilmiskevich.labs.space.model.Space;
import com.daniilmiskevich.labs.space.repository.SpaceRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class SpaceService {

    private final SpaceRepository repository;

    public SpaceService(SpaceRepository repository) {
        this.repository = repository;
    }

    public List<Space> findAll() {
        return repository.findAll();
    }

    public Optional<Space> findById(Long id) {
        return repository.findById(id);
    }

    public Optional<Space> findByName(String name) {
        return repository.findByName(name);
    }

    public List<Space> matchByName(String pattern) {
        var regexp = pattern
                .replace("*", "%");

        return repository.matchByName(regexp);
    }

    public Space create(Space space) {
        return repository.save(space);
    }

    public Space update(Space space) {
        if (!repository.existsById(space.getId())) {
            throw new EntityNotFoundException();
        }
        return repository.save(space);
    }

    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}
