package com.fm.FridgeMates.controllers;

import com.fm.FridgeMates.models.ApplicationUser;
import com.fm.FridgeMates.repositories.ApplicationUserRepository;
import com.fm.FridgeMates.repositories.RefrigeratorRepository;
import com.fm.FridgeMates.repositories.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;
import java.util.List;

@Controller
public class AdminPanelController {

    @Autowired
     ApplicationUserRepository applicationUserRepository;

    @Autowired
     RefrigeratorRepository refrigeratorRepository;

    @Autowired
     IngredientRepository ingredientRepository;

    @GetMapping("/admin")
    public String adminPanel(Model model, Principal p) {
        if (p!=null) {
            String username = p.getName();
            ApplicationUser browsingUser = applicationUserRepository.findByUsername(username);
            model.addAttribute("browsingUser", browsingUser);
        }
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
                applicationUserRepository.delete(user);
            }
        }
        return "redirect:/admin";
    }
}
