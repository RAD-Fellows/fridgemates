package com.fm.FridgeMates.controllers;

import com.fm.FridgeMates.models.ApplicationUser;
import com.fm.FridgeMates.models.Ingredient;
import com.fm.FridgeMates.models.Refrigerator;
import com.fm.FridgeMates.repositories.ApplicationUserRepository;
import com.fm.FridgeMates.repositories.CommentRepository;
import com.fm.FridgeMates.repositories.IngredientRepository;
import com.fm.FridgeMates.repositories.RefrigeratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Controller
public class RefrigeratorController {
    @Autowired
    ApplicationUserRepository applicationUserRepository;

    @Autowired
    RefrigeratorRepository refrigeratorRepository;

    @Autowired
    IngredientRepository ingredientRepository;

    @Autowired
    CommentRepository commentRepository;

    @GetMapping("/refrigerator/{id}")
    public String getRefrigeratorPage(Model m, Principal p, @PathVariable Long id){
        if (p != null ){
            String username = p.getName();
            ApplicationUser browsingUser = applicationUserRepository.findByUsername(username);
            m.addAttribute("username", username);
        }

        ApplicationUser foundUser = applicationUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        m.addAttribute("foundUser", foundUser);

        return "refrigerators";
    }

    @PostMapping("/refrigerator/add-ingredient")
    public RedirectView addIngredient(Principal p, String name, String description, LocalDate dateAdded){

        if (p != null){
            String username = p.getName();
            ApplicationUser browsingUser = applicationUserRepository.findByUsername(username);

            Ingredient newIngredient = new Ingredient(name, description, dateAdded, browsingUser.getRefrigerator());
            ingredientRepository.save(newIngredient);

            Refrigerator userRefrigerator = browsingUser.getRefrigerator();
            userRefrigerator.getIngredients().add(newIngredient);
            refrigeratorRepository.save(userRefrigerator);
            return new RedirectView("/refrigerator/"+browsingUser.getId());
        }
        return new RedirectView("/");
    }



    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public class ResourceNotFoundException extends RuntimeException {
        ResourceNotFoundException(String message) {
            super(message);
        }
    }
}
