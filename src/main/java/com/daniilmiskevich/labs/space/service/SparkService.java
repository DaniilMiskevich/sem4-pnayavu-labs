package com.daniilmiskevich.labs.space.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.query.EscapeCharacter;
import org.springframework.stereotype.Service;

import com.daniilmiskevich.labs.space.model.Spark;
import com.daniilmiskevich.labs.space.repository.SpaceRepository;
import com.daniilmiskevich.labs.space.repository.SparkRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class SparkService {

    private final SparkRepository repository;
    private final SpaceRepository spaceRepository;

    public SparkService(SparkRepository repository, SpaceRepository spaceRepository) {
        this.repository = repository;
        this.spaceRepository = spaceRepository;
    }

    public List<Spark> findAll() {
        return repository.findAll();
    }

    public Optional<Spark> findById(Long id) {
        return repository.findById(id);
    }

    public List<Spark> matchByName(String pattern) {
        pattern = EscapeCharacter.DEFAULT.escape(pattern);
        var jpqlPattern = String.format("*%s*", pattern).replace("*", "%");

        return repository.matchByName(jpqlPattern);
    }

    public Spark create(Long spaceId, Spark spark) {
        var optionalSpace = spaceRepository.findById(spaceId);
        if (optionalSpace.isEmpty()) {
            throw new EntityNotFoundException();
        }
        var space = optionalSpace.get();

        space.getSparks().add(spark);
        spark.setSpace(space);

        return repository.save(spark);
    }

    public Spark update(Spark partialSpark) {
        var optionalSpark = repository.findById(partialSpark.getId());
        if (optionalSpark.isEmpty()) {
            throw new EntityNotFoundException();
        }
        var spark = optionalSpark.get();

        if (partialSpark.getName() != null) {
            spark.setName(partialSpark.getName());
        }

        return repository.save(spark);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}
