package com.daniilmiskevich.labs.space.service;

import com.daniilmiskevich.labs.space.cache.SparkCache;
import com.daniilmiskevich.labs.space.model.Space;
import com.daniilmiskevich.labs.space.model.Spark;
import com.daniilmiskevich.labs.space.model.Spectre;
import com.daniilmiskevich.labs.space.repository.SpaceRepository;
import com.daniilmiskevich.labs.space.repository.SparkRepository;
import com.daniilmiskevich.labs.space.repository.SpectreRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.rsocket.RSocketProperties;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SparkServiceTest {

    private final SparkCache cache = new SparkCache(1);

    @Mock
    private SparkRepository repository;
    @Mock
    private SpaceRepository spaceRepository;
    @Mock
    private SpectreRepository spectreRepository;

    private SparkService service;

    @BeforeEach
    void setUp() {
        service = new SparkService(repository, spaceRepository, spectreRepository, cache);
    }

    @Test
    void findByIdReturnsSpark() {
        // Arrange
        var id = 1L;
        var expectedSpark = new Spark();
        when(repository.findById(id)).thenReturn(Optional.of(expectedSpark));

        // Act
        var actualSpark = service.findById(id);

        // Assert
        assertTrue(actualSpark.isPresent());
        assertEquals(expectedSpark, actualSpark.get());
        verify(repository, times(1)).findById(id);
    }

    @Test
    void matchForNullNamePatternAndNullSpectrePatternReturnsAllSparks() {
        // Arrange
        var expectedSparks = List.of(new Spark(), new Spark());
        when(repository.findAll()).thenReturn(expectedSparks);

        // Act
        var actualSparks = service.match(null, null);

        // Assert
        assertEquals(expectedSparks, actualSparks);
        verify(repository, times(1)).findAll();
    }

    @Test
    void matchForNullNamePatternAndNonNullSpectrePatternReturnsMatchingSparks() {
        // Arrange
        var jpqlNamePattern = "%%%";
        var spectrePattern = "foo,bar";
        var spectreNames = Set.of("foo", "bar");
        var expectedSparks = List.of(new Spark(), new Spark());
        when(repository.match(jpqlNamePattern, spectreNames)).thenReturn(expectedSparks);

        // Act
        var actualSparks = service.match(null, spectrePattern);

        // Assert
        assertEquals(expectedSparks, actualSparks);
        verify(repository, times(1)).match(jpqlNamePattern, spectreNames);
    }

    @Test
    void matchForNonNullNamePatternAndNullSpectrePatternReturnsMatchingSparks() {
        // Arrange
        var namePattern = "foo";
        var jpqlNamePattern = "%foo%";
        var spectreNames = Set.<String>of();
        var expectedSparks = List.of(new Spark(), new Spark());
        when(repository.match(jpqlNamePattern, spectreNames)).thenReturn(expectedSparks);

        // Act
        var actualSparks = service.match(namePattern, null);

        // Assert
        assertEquals(expectedSparks, actualSparks);
        verify(repository, times(1)).match(jpqlNamePattern, spectreNames);
    }

    @Test
    void matchForNonNullNamePatternAndNonNullSpectrePatternReturnsMatchingSparks() {
        // Arrange
        var namePattern = "foo";
        var spectrePattern = "foo,bar";
        var spectreNames = Arrays.stream(spectrePattern.split(",")).collect(Collectors.toSet());
        var jpqlNamePattern = "%foo%";
        var expectedSparks = List.of(new Spark(), new Spark());
        when(repository.match(jpqlNamePattern, spectreNames)).thenReturn(expectedSparks);

        // Act
        var actualSparks = service.match(namePattern, spectrePattern);

        // Assert
        assertEquals(expectedSparks, actualSparks);
        assertEquals(actualSparks, cache.getByNamePatternAndSpectreNames(namePattern, spectreNames));
        verify(repository, times(1)).match(jpqlNamePattern, spectreNames);
    }

    @Test
    void matchForCachedPatternsReturnsCachedSpark() {
        // Arrange
        var namePattern = "*";
        var spectrePattern = "foo,bar";
        var spectreNames = Arrays.stream(spectrePattern.split(",")).collect(Collectors.toSet());
        var jpqlNamePattern = "%%%";
        var expectedSparks = List.of(new Spark(), new Spark());
        cache.putByNamePatternAndSpectreNames(namePattern, spectreNames, expectedSparks);

        // Act
        var actualSparks = service.match(namePattern, spectrePattern);

        // Assert
        assertEquals(expectedSparks, actualSparks);
        verifyNoInteractions(repository);
    }

    @Test
    void createCreatesSpark() {
        // Arrange
        var spaceId = 1L;
        var space = new Space();
        space.setSparks(new ArrayList<>());

        var spectres = Set.of(new Spectre("foo"), new Spectre("bar"));
        var spectreNames = spectres.stream().map(Spectre::getName).collect(Collectors.toSet());
        var spark = new Spark(null, "spark", spectres);

        var savedSpark = new Spark(12L, spark.getName(), spark.getSpectres());

        when(spaceRepository.findById(spaceId)).thenReturn(Optional.of(space));
        when(spectreRepository.saveAll(spectres)).thenReturn(List.copyOf(spectres));
        when(repository.save(spark)).thenReturn(savedSpark);

        cache.putByNamePatternAndSpectreNames(spark.getName(), spectreNames, List.of());

        // Act
        var actualSpark = service.create(spaceId, spark);

        // Assert
        assertEquals(savedSpark, actualSpark);
        assertEquals(space, spark.getSpace());
        assertTrue(space.getSparks().contains(spark));
        assertNull(cache.getByNamePatternAndSpectreNames(spark.getName(), spectreNames));
        verify(spaceRepository, times(1)).findById(spaceId);
        verify(spectreRepository, times(1)).saveAll(spectres);
        verify(repository, times(1)).save(spark);
    }

    @Test
    void createForNonExistentSpaceThrows() {
        // Arrange
        var spaceId = 1L;
        var spark = new Spark();
        when(spaceRepository.findById(spaceId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(EntityNotFoundException.class, () -> service.create(spaceId, spark));
        verify(spaceRepository, times(1)).findById(spaceId);
        verifyNoInteractions(spectreRepository);
        verifyNoInteractions(repository);
    }


    @Test
    void createAllCreatesSparks() {
        // Arrange
        var spaceId = 1L;
        var space = new Space();
        space.setSparks(new ArrayList<>());

        var spectres = Set.of(new Spectre("foo"), new Spectre("bar"));
        var sparks = List.of(new Spark(null, "spark", spectres));

        var savedSparks = sparks
            .stream()
            .map(spark -> new Spark(12L, spark.getName(), spark.getSpectres()))
            .toList();

        when(spaceRepository.findById(spaceId)).thenReturn(Optional.of(space));
        when(spectreRepository.saveAll(spectres)).thenReturn(List.copyOf(spectres));
        when(repository.saveAll(sparks)).thenReturn(savedSparks);

        // Act
        var actualSparks = service.createAll(spaceId, sparks);

        // Assert
        assertEquals(savedSparks, actualSparks);
        verify(spaceRepository, times(1)).findById(spaceId);
        verify(spectreRepository, times(1)).saveAll(spectres);
        verify(repository, times(1)).saveAll(sparks);
    }

    @Test
    void createAllForNonExistentSpaceThrows() {
        // Arrange
        var spaceId = 1L;
        var sparks = List.of(new Spark());
        when(spaceRepository.findById(spaceId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(EntityNotFoundException.class, () -> service.createAll(spaceId, sparks));
        verify(spaceRepository, times(1)).findById(spaceId);
        verifyNoInteractions(spectreRepository);
        verifyNoInteractions(repository);
    }

    @Test
    void updateUpdatesSpark() {
        // Arrange
        var sparkId = 1L;

        var oldSpectres = new HashSet<Spectre>();
        var oldSpark = new Spark(sparkId, "old name", oldSpectres);
        var oldSpectre = new Spectre("foo");
        oldSpectres.add(oldSpectre);
        oldSpectre.setSparksWithin(List.of(oldSpark));

        var newSpectres = new HashSet<Spectre>();
        var partialSpark = new Spark(sparkId, "new name", newSpectres);
        var newSpectre = new Spectre("bar");
        newSpectres.add(oldSpectre);
        newSpectre.setSparksWithin(List.of(oldSpark));

        var updatedSpark = new Spark(oldSpark.getId(), partialSpark.getName(), partialSpark.getSpectres());

        when(repository.findById(sparkId)).thenReturn(Optional.of(oldSpark));
        when(spectreRepository.saveAll(newSpectres)).thenReturn(List.copyOf(newSpectres));
        when(repository.save(oldSpark)).thenReturn(updatedSpark);

        // Act
        var actualSpark = service.update(partialSpark);

        // Assert
        assertEquals(updatedSpark, actualSpark);
        assertNotEquals(oldSpectre, newSpectre);
        verify(repository, times(1)).findById(sparkId);
        verify(spectreRepository, times(1)).saveAll(newSpectres);
        verify(repository, times(1)).save(oldSpark);
    }

    @Test
    void updateNonExistingSparkThrows() {
        // Arrange
        var sparkId = 1L;
        var partialSpark = new Spark(sparkId, "new name", null);
        when(repository.findById(sparkId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(EntityNotFoundException.class, () -> service.update(partialSpark));
        verify(repository, times(1)).findById(sparkId);
        verifyNoInteractions(spectreRepository);
    }

    @Test
    void deleteByIdDeletes() {
        // Arrange
        var id = 1L;
        var existingSpark = new Spark(id, "to delete", Set.of());
        when(repository.findById(id)).thenReturn(Optional.of(existingSpark));
        doNothing().when(repository).deleteById(id);

        // Act
        service.deleteById(id);

        // Assert
        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).deleteById(id);
    }
}