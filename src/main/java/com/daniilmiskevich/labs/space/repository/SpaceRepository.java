package com.daniilmiskevich.labs.space.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.daniilmiskevich.labs.space.model.Space;

@Repository
public interface SpaceRepository {

    List<Space> findAll();

    Optional<Space> findByName(String name);

    List<Space> matchByName(String regexp);

    void create(Space space);

    void updateByName(String name, Space space);

    void deleteByName(String name);

}
