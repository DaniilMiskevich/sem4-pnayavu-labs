package com.daniilmiskevich.labs.space.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.daniilmiskevich.labs.space.model.Spectre;
import com.daniilmiskevich.labs.space.repository.SparkRepository;
import com.daniilmiskevich.labs.space.repository.SpectreRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class SpectreService {

    private final SpectreRepository repository;
    private final SparkRepository sparkRepository;

    public SpectreService(SpectreRepository repository, SparkRepository sparkRepository) {
        this.repository = repository;
        this.sparkRepository = sparkRepository;
    }

    public Optional<Spectre> findByName(String name) {
        return repository.findByName(name);
    }

    public Spectre addSparkByName(String name, Long sparkId) {
        var optionalSpark = sparkRepository.findById(sparkId);
        if (optionalSpark.isEmpty()) {
            throw new EntityNotFoundException();
        }
        var spark = optionalSpark.get();

        var optionalSpectre = repository.findByName(name);
        if (optionalSpectre.isEmpty()) {
            optionalSpectre = Optional.of(new Spectre(name));
        }
        var spectre = optionalSpectre.get();

        spectre.addSpark(spark);
        spark.addSpectre(spectre);
        return repository.save(spectre);
    }

    public void deleteSparkByName(String name, Long sparkId) {
        repository.deleteByName(name);
    }

}
