package com.daniilmiskevich.labs.space.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.daniilmiskevich.labs.space.model.Space;

@Repository
public interface SpaceRepository {

    public List<Space> findAll();

    public Optional<Space> findByName(String name);

    public List<Space> matchByName(String regexp);

}
