package com.daniilmiskevich.labs.space.model;

import java.util.Set;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@Entity
public class Spark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_id")
    private Space space;

    @ManyToMany(mappedBy = "sparksWithin", cascade = CascadeType.DETACH)
    private Set<Spectre> spectres;

    public Spark() {
        this.id = null;
    }

    public Spark(Long id, String name) {
        this.id = id;
        this.name = name;
        this.space = null;
        this.spectres = null;
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

    public Set<Spectre> getSpectres() {
        return spectres;
    }

    public void setName(String value) {
        name = value;
    }

    public void setSpace(Space value) {
        space = value;
    }

    public void setSpectres(Set<Spectre> spectres) {
        this.spectres = spectres;
    }

}
