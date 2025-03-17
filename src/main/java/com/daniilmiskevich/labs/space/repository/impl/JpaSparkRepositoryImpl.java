package com.daniilmiskevich.labs.space.repository.impl;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.daniilmiskevich.labs.space.model.Spark;
import com.daniilmiskevich.labs.space.repository.SparkRepository;
import jakarta.transaction.Transactional;

@Repository
public interface JpaSparkRepositoryImpl extends SparkRepository, JpaRepository<Spark, String> {

    default List<Spark> match(String jpqlPattern, Set<String> spectreNames) {
        return spectreNames.isEmpty()
            ? matchByName(jpqlPattern)
            : matchByNameAndSpectreNames(jpqlPattern, spectreNames);
    }

    @Query(value = "SELECT s FROM Spark s WHERE (LOWER(s.name) LIKE LOWER(:pattern))")
    List<Spark> matchByName(@Param("pattern") String jpqlPattern);

    @Query(value = "SELECT s FROM Spark s LEFT JOIN s.spectres sp "
        + "WHERE (LOWER(s.name) LIKE LOWER(:pattern)) "
        + "AND (sp.name IN :spectreNames)")
    List<Spark> matchByNameAndSpectreNames(
        @Param("pattern") String jpqlPattern,
        Set<String> spectreNames);

    @Transactional
    void deleteById(Long id);

}
