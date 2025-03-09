package com.daniilmiskevich.labs.space.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.daniilmiskevich.labs.space.model.Spectre;

@Repository
public interface SpectreRepository {

    Optional<Spectre> findByName(String name);

    Spectre save(Spectre space);

    void deleteByName(String name);

}
