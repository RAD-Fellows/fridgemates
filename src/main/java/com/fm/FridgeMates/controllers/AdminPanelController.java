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
    private ApplicationUserRepository applicationUserRepository;

    @GetMapping("/admin")
    public String adminPanel(Model model, Principal p) {
        if (p != null) {
            String username = p.getName();
            ApplicationUser browsingUser = applicationUserRepository.findByUsername(username);
            model.addAttribute("browsingUser", browsingUser);
        }
        List<ApplicationUser> users = applicationUserRepository.findAll();
        model.addAttribute("users", users);
        return "admin";
    }


    @GetMapping("/myprofile/{id}")
    public String viewUserInfo(Model model, @PathVariable Long id) {
        ApplicationUser browsingUser = applicationUserRepository.findById(id).orElseThrow();
        Refrigerator userRefrigerator = browsingUser.getRefrigerator();
        model.addAttribute("browsingUser", browsingUser);
        model.addAttribute("browsingUserRefrigerator", userRefrigerator);
        return "myprofile.html";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam Long userId, Principal principal) {
        Optional<ApplicationUser> userToDelete = applicationUserRepository.findById(userId);
        if (userToDelete.isPresent()) {
            ApplicationUser user = userToDelete.get();
            String loggedInUsername = principal.getName();
            String username = user.getUsername();

            if (loggedInUsername.equals(username) && !username.equals("Admin") && !username.equals("Admin1")) {
                // Allow users to delete their own profile (except Admin and Admin1)
                applicationUserRepository.delete(user);
            }
        }
        return "redirect:/admin";
    }

    @PostMapping("/update-profile/{id}")
    public String updateUserProfile(@PathVariable Long id,
                                    @RequestParam String firstName,
                                    @RequestParam String lastName,
                                    @RequestParam LocalDate dateOfBirth,
                                    @RequestParam String address,
                                    @RequestParam String city,
                                    @RequestParam String state,
                                    @RequestParam Integer zip) {
        ApplicationUser userToUpdate = applicationUserRepository.findById(id).orElseThrow();
        userToUpdate.setFirstName(firstName);
        userToUpdate.setLastName(lastName);
        userToUpdate.setDateOfBirth(dateOfBirth);
        userToUpdate.setAddress(address);
        userToUpdate.setCity(city);
        userToUpdate.setState(state);
        userToUpdate.setZip(zip);
        applicationUserRepository.save(userToUpdate);
        return "redirect:/myprofile/" + id;
    }
}

