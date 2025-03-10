package com.daniilmiskevich.labs.space.model;

import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

@Entity
public class Spectre {

    @Id
    private final String name;

    @ManyToMany(cascade = CascadeType.DETACH)
    @JoinTable(
        name = "spectre_sparks",
        joinColumns = @JoinColumn(name = "spectre_id"),
        inverseJoinColumns = @JoinColumn(name = "spark_id"))
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
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Spectre other = (Spectre) obj;
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
