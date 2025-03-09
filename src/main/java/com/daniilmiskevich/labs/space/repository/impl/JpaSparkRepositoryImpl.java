package com.daniilmiskevich.labs.space.repository.impl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.daniilmiskevich.labs.space.model.Spark;
import com.daniilmiskevich.labs.space.repository.SparkRepository;

import jakarta.transaction.Transactional;

@Repository
public interface JpaSparkRepositoryImpl extends SparkRepository, JpaRepository<Spark, String> {

    @Query(value = "SELECT s FROM Spark s WHERE LOWER(s.name) LIKE LOWER(:jpql_pattern)")
    List<Spark> matchByName(String jpql_pattern);

    @Transactional
    void deleteById(Long id);

}
