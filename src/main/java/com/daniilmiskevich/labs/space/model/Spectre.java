package com.daniilmiskevich.labs.space.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@Entity
public class Spectre {

    @Id
    private final String name;

    @ManyToMany(mappedBy = "spectres", cascade = CascadeType.DETACH)
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

    public void setSparksWithin(List<Spark> sparksWithin) {
        this.sparksWithin = sparksWithin;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (getClass() != obj.getClass()) {
            return false;
        }

        var other = (Spectre) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }

        return true;
    }

}
