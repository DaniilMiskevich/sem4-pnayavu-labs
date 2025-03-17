package com.daniilmiskevich.labs.space.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.query.EscapeCharacter;
import org.springframework.stereotype.Service;

import com.daniilmiskevich.labs.space.model.Spark;
import com.daniilmiskevich.labs.space.repository.SpaceRepository;
import com.daniilmiskevich.labs.space.repository.SparkRepository;
import com.daniilmiskevich.labs.space.repository.SpectreRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class SparkService {

    private final SparkRepository repository;
    private final SpaceRepository spaceRepository;
    private final SpectreRepository spectreRepository;

    public SparkService(SparkRepository repository, SpaceRepository spaceRepository,
                        SpectreRepository spectreRepository) {
        this.repository = repository;
        this.spaceRepository = spaceRepository;
        this.spectreRepository = spectreRepository;
    }

    public Optional<Spark> findById(Long id) {
        return repository.findById(id);
    }

    public List<Spark> match(String namePattern, String spectrePattern) {
        if (namePattern == null && spectrePattern == null) {
            return repository.findAll();
        } else {
            if (namePattern == null) {
                namePattern = "*";
            }
            if (spectrePattern == null) {
                spectrePattern = "";
            }

            namePattern = EscapeCharacter.DEFAULT.escape(namePattern);
            var jpqlNamePattern = String.format("*%s*", namePattern).replace("*", "%");

            var spectreNames = !spectrePattern.isEmpty()
                ? Arrays.stream(spectrePattern.split(","))
                .collect(Collectors.toSet())
                : Set.<String>of();

            return repository.match(jpqlNamePattern, spectreNames);
        }
    }

    @Transactional
    public Spark create(Long spaceId, Spark spark) {
        var space = spaceRepository.findById(spaceId)
            .orElseThrow(EntityNotFoundException::new);

        space.getSparks().add(spark);
        spark.setSpace(space);

        spark.getSpectres().forEach(spectreRepository::save);

        return repository.save(spark);
    }

    @Transactional
    public Spark update(Spark partialSpark) {
        var spark = repository.findById(partialSpark.getId())
            .orElseThrow(EntityNotFoundException::new);

        if (partialSpark.getName() != null) {
            spark.setName(partialSpark.getName());
        }

        if (partialSpark.getSpectres() != null) {
            var addedSpectres = partialSpark.getSpectres()
                .stream()
                .filter(partialSpectre -> !spark.getSpectres().contains(partialSpectre))
                .map(partialSpectre -> {
                    var spectre = spectreRepository.findByName(partialSpectre.getName())
                        .orElse(partialSpectre);
                    if (spectre.getSparksWithin() == null) {
                        spectre.setSparksWithin(new ArrayList<>());
                    }
                    return spectre;
                })
                .collect(Collectors.toSet());
            var removedSpectres = spark.getSpectres()
                .stream()
                .filter(spectre -> !partialSpark.getSpectres().contains(spectre))
                .collect(Collectors.toSet());

            spark.getSpectres().addAll(addedSpectres);
            spark.getSpectres().removeAll(removedSpectres);
            addedSpectres.forEach(spectre -> spectre.getSparksWithin().add(spark));
            removedSpectres.forEach(spectre -> spectre.getSparksWithin().remove(spark));

            spark.getSpectres().forEach(spectreRepository::save);
        }

        return repository.save(spark);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}
