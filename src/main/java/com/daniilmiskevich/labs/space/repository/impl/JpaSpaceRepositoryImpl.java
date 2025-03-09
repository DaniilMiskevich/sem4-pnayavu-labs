package com.daniilmiskevich.labs.space.repository.impl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.daniilmiskevich.labs.space.model.Space;
import com.daniilmiskevich.labs.space.repository.SpaceRepository;

import jakarta.transaction.Transactional;

@Repository
public interface JpaSpaceRepositoryImpl extends SpaceRepository, JpaRepository<Space, String> {

    @Query(value = "SELECT s FROM Space s WHERE s.name LIKE :pattern")
    List<Space> matchByName(@Param("pattern") String jpqlPattern);

    @Transactional
    void deleteById(Long id);

}
