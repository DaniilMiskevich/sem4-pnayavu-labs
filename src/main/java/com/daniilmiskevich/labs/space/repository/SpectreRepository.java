package com.daniilmiskevich.labs.space.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.daniilmiskevich.labs.space.model.Spectre;

@Repository
public interface SpectreRepository {

    Optional<Spectre> findByName(String name);

    boolean existsByName(String name);

    Spectre save(Spectre space);

    <S extends Spectre> List<S> saveAll(Iterable<S> space);

    void deleteByName(String name);

}
