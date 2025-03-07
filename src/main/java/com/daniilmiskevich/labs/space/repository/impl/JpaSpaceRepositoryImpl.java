package com.daniilmiskevich.labs.space.repository.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daniilmiskevich.labs.space.model.Space;
import com.daniilmiskevich.labs.space.repository.SpaceRepository;

@Repository
public interface JpaSpaceRepositoryImpl extends SpaceRepository, JpaRepository<Space, String> {

}
