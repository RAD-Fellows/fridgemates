package com.fm.FridgeMates.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@Entity
public class Refrigerator implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @OneToMany(mappedBy = "refrigerator" , cascade = CascadeType.ALL, orphanRemoval = true)
    List<Ingredient> ingredients;
    @OneToMany(mappedBy = "refrigerator" , cascade = CascadeType.ALL, orphanRemoval = true)
    List<Comment> comments;



    public Refrigerator() {
    }

    public Refrigerator(List<Ingredient> ingredients, List<Comment> comments) {
        this.ingredients = ingredients;
        this.comments = comments;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
