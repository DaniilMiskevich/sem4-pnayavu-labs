package com.daniilmiskevich.labs.space.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.daniilmiskevich.labs.space.model.Spark;

@Repository
public interface SparkRepository {

    List<Spark> findAll();

    Optional<Spark> findById(Long id);

    /**
     * @param jpqlPattern - jpql-like pattern, where `_` matches any character and `%` matches any
     *                    sequence of characters.
     */
    List<Spark> match(String jpqlPattern, Set<String> spectreNames);

    Spark save(Spark spark);

    void deleteById(Long id);

}
