package com.daniilmiskevich.labs.space.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Spark {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne
    private Space space;

    public Spark(Long id, String name, Space space) {
        this.id = id;
        this.name = name;
        this.space = space;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Space getSpace() {
        return space;
    }

}
