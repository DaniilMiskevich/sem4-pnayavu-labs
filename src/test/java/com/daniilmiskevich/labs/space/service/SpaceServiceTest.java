package com.daniilmiskevich.labs.space.service;

import com.daniilmiskevich.labs.space.model.Space;
import com.daniilmiskevich.labs.space.repository.SpaceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpaceServiceTest {

    @Mock
    private SpaceRepository repository;

    @InjectMocks
    private SpaceService service;

    @Test
    void findByIdReturnsSpace() {
        // Arrange
        var id = 1L;
        var expectedSpace = new Space(id, "foo");
        when(repository.findById(id)).thenReturn(Optional.of(expectedSpace));

        // Act
        var actualSpace = service.findById(id);

        // Assert
        assertTrue(actualSpace.isPresent());
        assertEquals(expectedSpace, actualSpace.get());
        verify(repository, times(1)).findById(id);
    }

    @Test
    void findByNameReturnsSpace() {
        // Arrange
        var name = "foo";
        var expectedSpace = new Space(1L, name);
        when(repository.findByName(name)).thenReturn(Optional.of(expectedSpace));

        // Act
        var actualSpace = service.findByName(name);

        // Assert
        assertTrue(actualSpace.isPresent());
        assertEquals(expectedSpace, actualSpace.get());
        verify(repository, times(1)).findByName(name);
    }

    @Test
    void matchForNullNamePatternReturnsAllSpaces() {
        // Arrange
        var expectedSpaces = List.of(new Space(1L, "foo"), new Space(2L, "bar"));
        when(repository.findAll()).thenReturn(expectedSpaces);

        // Act
        var actualSpaces = service.match(null);

        // Assert
        assertEquals(expectedSpaces, actualSpaces);
        verify(repository, times(1)).findAll();
    }

    @Test
    void matchForNonNullNamePatternReturnsMatchingSparks() {
        // Arrange
        var expectedSpaces = List.of(new Space(1L, "bar"), new Space(1L, "baz"));
        when(repository.matchByName("b%")).thenReturn(expectedSpaces);

        // Act
        var actualSpaces = service.match("b*");

        // Assert
        assertEquals(expectedSpaces, actualSpaces);
        verify(repository, times(1)).matchByName("b%");
    }

    @Test
    void createCreatesSpace() {
        // Arrange
        var spaceToCreate = new Space(null, "foo");
        var savedSpace = new Space(1L, spaceToCreate.getName());
        savedSpace.setSparks(List.of());
        when(repository.save(spaceToCreate)).thenReturn(savedSpace);

        // Act
        var actualSpace = service.create(spaceToCreate);

        // Assert
        assertEquals(savedSpace, actualSpace);
        verify(repository, times(1)).save(spaceToCreate);
    }

    @Test
    void updateUpdatesSpace() {
        // Arrange
        var id = 1L;
        var oldSpace = new Space(id, "old-space");
        var partialSpace = new Space(id, "new-space");
        var updatedSpace = new Space(oldSpace.getId(), partialSpace.getName());
        when(repository.findById(id)).thenReturn(Optional.of(oldSpace));
        when(repository.save(oldSpace)).thenReturn(updatedSpace);

        // Act
        var actualSpace = service.update(partialSpace);

        // Assert
        assertEquals(updatedSpace, actualSpace);
        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).save(oldSpace);
    }

    @Test
    void updateNonExistingSpaceThrows() {
        // Arrange
        var id = 1L;
        var partialSpace = new Space(id, null);
        when(repository.findById(id)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(EntityNotFoundException.class, () -> service.update(partialSpace));
        verify(repository, times(1)).findById(id);
    }

    @Test
    void deleteByIdDeletes() {
        // Arrange
        var id = 1L;
        doNothing().when(repository).deleteById(id);

        // Act
        service.deleteById(id);

        // Assert
        verify(repository, times(1)).deleteById(id);
    }
}