package com.fm.FridgeMates.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
public class Ingredient implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String ingredientName;

    String description;

    LocalDate dateAdded;

    @ManyToOne
    Refrigerator refrigerator;

    public Ingredient() {
    }

    public Ingredient(String ingredientName, String description, LocalDate dateAdded, Refrigerator refrigerator) {
        this.ingredientName = ingredientName;
        this.description = description;
        this.dateAdded = dateAdded;
        this.refrigerator = refrigerator;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }

    public Refrigerator getRefrigerator() {
        return refrigerator;
    }

    public void setRefrigerator(Refrigerator refrigerator) {
        this.refrigerator = refrigerator;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "ingredientName='" + ingredientName + '\'' +
                ", description='" + description + '\'' +
                ", dateAdded=" + dateAdded +
                ", refrigerator=" + refrigerator +
                '}';
    }
}
