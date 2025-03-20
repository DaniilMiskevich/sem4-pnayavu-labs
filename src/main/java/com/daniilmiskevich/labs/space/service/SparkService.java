package com.daniilmiskevich.labs.space.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.hibernate.Hibernate;
import org.springframework.data.jpa.repository.query.EscapeCharacter;
import org.springframework.stereotype.Service;
import com.daniilmiskevich.labs.space.cache.SparkCache;
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

    private final SparkCache cache;

    public SparkService(
        SparkRepository repository,
        SpaceRepository spaceRepository,
        SpectreRepository spectreRepository,
        SparkCache cache) {
        this.repository = repository;
        this.spaceRepository = spaceRepository;
        this.spectreRepository = spectreRepository;

        this.cache = cache;
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

            var cached = cache.getByNamePatternAndSpectreNames(namePattern, spectreNames);
            if (cached != null) {
                return cached;
            }

            var fetched = repository.match(jpqlNamePattern, spectreNames);
            cache.putByNamePatternAndSpectreNames(namePattern, spectreNames, fetched);
            return fetched;
        }
    }

    @Transactional
    public Spark create(Long spaceId, Spark spark) {
        var space = spaceRepository.findById(spaceId)
            .orElseThrow(EntityNotFoundException::new);

        space.getSparks().add(spark);
        spark.setSpace(space);

        spark.getSpectres().forEach(spectreRepository::save);

        var newSpark = repository.save(spark);
        cache.invalidateByNameAndSpectreNames(
            newSpark.getName(),
            newSpark.getSpectres()
                .stream()
                .map(spectre -> spectre.getName())
                .collect(Collectors.toSet()));
        return newSpark;
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

        var newSpark = repository.save(spark);
        cache.invalidateByNameAndSpectreNames(
            newSpark.getName(),
            newSpark.getSpectres()
                .stream()
                .map(spectre -> spectre.getName())
                .collect(Collectors.toSet()));
        return newSpark;
    }

    @Transactional
    public void deleteById(Long id) {
        var spark = repository.findById(id);
        if (spark.isEmpty()) {
            return;
        }
        Hibernate.initialize(spark.get().getSpectres());

        repository.deleteById(id);

        cache.invalidateByNameAndSpectreNames(
            spark.get().getName(),
            spark.get().getSpectres()
                .stream()
                .map(spectre -> spectre.getName())
                .collect(Collectors.toSet()));
    }

}
