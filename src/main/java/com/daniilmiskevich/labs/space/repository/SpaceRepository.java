// https://hackernoon.com/using-postgres-effectively-in-spring-boot-applications

package com.daniilmiskevich.labs.space.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.daniilmiskevich.labs.space.model.Space;

@Repository
public interface SpaceRepository {

    List<Space> findAll();

    Optional<Space> findById(Long id);

    Optional<Space> findByName(String name);

    /**
     * @param jpqlPattern - jpql-like pattern, where `_` matches any character and `%` matches any
     *                    sequence of characters.
     */
    List<Space> matchByName(String jpqlPattern);

    Space save(Space space);

    void deleteById(Long id);

}
