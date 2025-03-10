package com.daniilmiskevich.labs.space.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.query.EscapeCharacter;
import org.springframework.stereotype.Service;

import com.daniilmiskevich.labs.space.model.Space;
import com.daniilmiskevich.labs.space.repository.SpaceRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class SpaceService {

    private final SpaceRepository repository;

    public SpaceService(SpaceRepository repository) {
        this.repository = repository;
    }

    public Optional<Space> findById(Long id) {
        return repository.findById(id);
    }

    public Optional<Space> findByName(String name) {
        return repository.findByName(name);
    }

    public List<Space> match(String namePattern) {
        if (namePattern == null) {
            return repository.findAll();
        } else {
            namePattern = EscapeCharacter.DEFAULT.escape(namePattern);
            var jpqlPattern = namePattern.replace("*", "%");

            return repository.matchByName(jpqlPattern);
        }
    }

    public Space create(Space space) {
        space.setSparks(List.of());

        return repository.save(space);
    }

    public Space update(Space partialSpace) {
        var space = repository.findById(partialSpace.getId())
            .orElseThrow(EntityNotFoundException::new);

        if (partialSpace.getName() != null) {
            space.setName(partialSpace.getName());
        }

        return repository.save(space);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}
