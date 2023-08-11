package com.fm.FridgeMates.repositories;

import com.fm.FridgeMates.models.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    void deleteById(Long id);
}
