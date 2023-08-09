package com.fm.FridgeMates.controllers;

import com.fm.FridgeMates.models.ApplicationUser;
import com.fm.FridgeMates.repositories.ApplicationUserRepository;
import com.fm.FridgeMates.repositories.RefrigeratorRepository;
import com.fm.FridgeMates.repositories.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.List;

@Controller
public class AdminPanelController {

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private RefrigeratorRepository refrigeratorRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    public AdminPanelController(ApplicationUserRepository applicationUserRepository, RefrigeratorRepository refrigeratorRepository, IngredientRepository ingredientRepository) {
        this.applicationUserRepository = applicationUserRepository;
        this.refrigeratorRepository = refrigeratorRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @GetMapping("/admin")
    public String adminPanel(Model model) {
        List<ApplicationUser> users = applicationUserRepository.findAll();
        model.addAttribute("users", users);
        return "admin";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam Long userId) {
        Optional<ApplicationUser> userToDelete = applicationUserRepository.findById(userId);
        if (userToDelete.isPresent()) {
            ApplicationUser user = userToDelete.get();
            if (!user.getUsername().equals("Admin1")) {
                // Delete user, refrigerator, and associated ingredients
                applicationUserRepository.delete(user);
            }
        }
        return "redirect:/admin";
    }
}
