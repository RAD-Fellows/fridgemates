package com.fm.FridgeMates.controllers;

import com.fm.FridgeMates.repositories.CommentRepository;
import com.fm.FridgeMates.repositories.IngredientRepository;
import com.fm.FridgeMates.repositories.RefrigeratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class RefrigeratorController {

    @Autowired
    RefrigeratorRepository refrigeratorRepository;

    @Autowired
    IngredientRepository ingredientRepository;

    @Autowired
    CommentRepository commentRepository;
}
