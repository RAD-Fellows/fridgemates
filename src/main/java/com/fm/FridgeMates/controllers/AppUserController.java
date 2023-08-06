package com.fm.FridgeMates.controllers;

import com.fm.FridgeMates.models.ApplicationUser;
import com.fm.FridgeMates.repositories.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class AppUserController {
    @Autowired
    ApplicationUserRepository applicationUserRepository;


//    @GetMapping("/login")
//    public String getLoginPage() { return "login.html";}
//
//    @GetMapping("/signup")
//    public String getSignUpPage(){return "signup.html";}

    @GetMapping("/")
    public String GetHomePage(Model m, Principal p) {
        if (p != null){
            String username = p.getName();
            ApplicationUser browsingUser = applicationUserRepository.findByUsername(username);
            m.addAttribute("browsingUser", browsingUser);
        }
        return "index.html";
    }
}
