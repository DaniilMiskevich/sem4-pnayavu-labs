package com.daniilmiskevich.labs.space.repository.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daniilmiskevich.labs.space.model.Spectre;
import com.daniilmiskevich.labs.space.repository.SpectreRepository;

import jakarta.transaction.Transactional;

@Repository
public interface JpaSpectreRepositoryImpl
    extends SpectreRepository, JpaRepository<Spectre, String> {

    @Transactional
    void deleteByName(String name);

}
