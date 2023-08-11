package com.fm.FridgeMates.controllers;

import com.fm.FridgeMates.models.ApplicationUser;
import com.fm.FridgeMates.models.Refrigerator;
import com.fm.FridgeMates.repositories.ApplicationUserRepository;
import com.fm.FridgeMates.repositories.RefrigeratorRepository;
import com.fm.FridgeMates.repositories.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.time.LocalDate;
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

    @GetMapping("/view-user/{id}")
    public String viewUser(@PathVariable Long id, Model model) {
        Optional<ApplicationUser> user = applicationUserRepository.findById(id);
        if (user.isPresent()) {
            model.addAttribute("viewedUser", user.get());
            return "view-user";
        }
        return "redirect:/admin";
    }

    @GetMapping("/myprofile/{id}")
    public String viewUserInfo(Model m, Principal p, @PathVariable Long id, String firstName, String lastName, LocalDate dateOfBirth, String address, String city, String state, Integer zip){
        if(p != null){
            ApplicationUser browsingUser = applicationUserRepository.findById(id).orElseThrow();
            Refrigerator userRefrigerator = browsingUser.getRefrigerator();
            m.addAttribute("browsingUser", browsingUser);
            m.addAttribute("browsingUserRefrigerator", userRefrigerator);
        }
        return "user-profile.html";
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
