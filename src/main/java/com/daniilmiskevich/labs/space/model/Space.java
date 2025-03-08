package com.daniilmiskevich.labs.space.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Space {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    final private Long id;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "space")
    private List<Spark> sparks;

    public Space() {
        this.id = null;
    }

    public Space(Long id, String name) {
        this.id = id;
        this.name = name;
        this.sparks = null;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Spark> getSparks() {
        return sparks;
    }

}
