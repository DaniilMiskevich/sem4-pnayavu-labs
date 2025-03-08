package com.daniilmiskevich.labs.space.repository.impl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.daniilmiskevich.labs.space.model.Space;
import com.daniilmiskevich.labs.space.repository.SpaceRepository;

@Repository
public interface JpaSpaceRepositoryImpl extends SpaceRepository, JpaRepository<Space, String> {

    @Query(value = "SELECT s FROM Space s WHERE s.name LIKE :jpql_pattern")
    List<Space> matchByName(String jpql_pattern);

}
