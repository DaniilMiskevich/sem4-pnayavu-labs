package com.daniilmiskevich.labs.space.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

@Entity
public class Spectre {

    @Id
    private final String name;

    @ManyToMany
    @JoinTable(name = "spectre_sparks", joinColumns = @JoinColumn(name = "spectre_id"), inverseJoinColumns = @JoinColumn(name = "spark_id"))
    private List<Spark> sparksWithin;

    public Spectre() {
        this.name = null;
    }

    public Spectre(String name) {
        this.name = name;
        this.sparksWithin = null;
    }

    public String getName() {
        return name;
    }

    public List<Spark> getSparksWithin() {
        return sparksWithin;
    }

    public void addSpark(Spark spark) {
        if (sparksWithin != null) {
            sparksWithin.add(spark);
        } else {
            sparksWithin = List.of(spark);
        }
    }

}
