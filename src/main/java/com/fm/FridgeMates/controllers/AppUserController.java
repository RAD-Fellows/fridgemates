package com.fm.FridgeMates.controllers;

import com.fm.FridgeMates.models.ApplicationUser;
import com.fm.FridgeMates.models.Refrigerator;
import com.fm.FridgeMates.repositories.ApplicationUserRepository;
import com.fm.FridgeMates.repositories.RefrigeratorRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Controller
public class AppUserController {
    @Autowired
    ApplicationUserRepository applicationUserRepository;
    @Autowired
    RefrigeratorRepository refrigeratorRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    HttpServletRequest request;


    @GetMapping("/login")
    public String getLoginPage() { return "login";}

    @GetMapping("/signup")
    public String getSignUpPage(Model m){
        List<String> usStates = Arrays.asList("AK", "AL", "AR", "AS", "AZ", "CA", "CO", "CT",
                "DC", "DE", "FL", "GA", "GU", "HI", "IA", "ID", "IL", "IN", "KS", "KY", "LA",
                "MA", "MD", "ME", "MI", "MN", "MO", "MP", "MS", "MT", "NC", "ND", "NE", "NH",
                "NJ", "NM", "NV", "NY", "OH", "OK", "OR", "PA", "PR", "RI", "SC", "SD", "TN",
                "TX", "UM", "UT", "VA", "VI", "VT", "WA", "WI", "WV", "WY");
        m.addAttribute("usStates", usStates);
        return "signup";
    }

    @GetMapping("/")
    public String GetHomePage(Model m, Principal p) {
        if (p != null){
            String username = p.getName();
            ApplicationUser browsingUser = applicationUserRepository.findByUsername(username);
            if (browsingUser != null) {
                m.addAttribute("browsingUser", browsingUser);
                System.out.println(browsingUser.toString());
            } else {
                m.addAttribute("browsingUser", null);
                System.out.println("browsing user is null");
            }
        } else {
            m.addAttribute("browsingUser", null);
            System.out.println("browsing user is null");
        }

        return "index";
    }

    @PostMapping("/signup")
    public RedirectView postSignUp(String firstName, String lastName, LocalDate dateOfBirth,
                                   String address, String city, String state, Integer zip,
                                   String username, String password, String imgUrl){
        String encryptedPassword=passwordEncoder.encode(password);

        Refrigerator newRefrigerator = new Refrigerator();
        refrigeratorRepository.save(newRefrigerator);

        ApplicationUser user = new ApplicationUser(username, encryptedPassword, firstName, lastName, dateOfBirth, address, city, state, zip, imgUrl);
        user.setRefrigerator(newRefrigerator);
        applicationUserRepository.save(user);

        return new RedirectView ("/");
    }

    @GetMapping("/myprofile")
    public String getProfile(Model m, Principal p) {
        if (p != null){
            String username = p.getName();
            ApplicationUser currentUser = applicationUserRepository.findByUsername(username);
            Refrigerator userRefrigerator = currentUser.getRefrigerator();
            m.addAttribute("currentUser", currentUser);
            m.addAttribute("currentUserRefrigerator", userRefrigerator);

        }
        return "user-profile.html";
    }
    

    @PutMapping("/myprofile/{id}")
    public RedirectView editUserInfo(Model m, Principal p, @PathVariable Long id, String firstName, String lastName, LocalDate dateOfBirth, String address, String city, String state, Integer zip){
        if(p != null){
            ApplicationUser currentUser = applicationUserRepository.findById(id).orElseThrow();

            currentUser.setFirstName(firstName);
            currentUser.setLastName(lastName);
            currentUser.setDateOfBirth(dateOfBirth);
            currentUser.setAddress(address);
            currentUser.setCity(city);
            currentUser.setState(state);
            currentUser.setZip(zip);
            applicationUserRepository.save(currentUser);
            m.addAttribute("currentUser", currentUser);
        }
        return new RedirectView("/myprofile");
    }

    @GetMapping("/users")
    public String getAllUsers(Model m, Principal p){
        if (p != null){
            String username = p.getName();
            ApplicationUser currentUser = applicationUserRepository.findByUsername(username);
            m.addAttribute("browsingUser", currentUser);

            List<ApplicationUser> usersInSameCity = applicationUserRepository.findByCityAndIdNot(currentUser.getCity(), currentUser.getId());
            for (ApplicationUser user : usersInSameCity) {
                System.out.println(user.getUsername() + user.getCity());
            }

            m.addAttribute("users", usersInSameCity);
            return "user-search.html";
        }
        return "index.html";
    }


    public void authWithHttpServletRequest(String username, String password){
        try {
            request.login(username, password);
        } catch(ServletException e){
            System.out.println("Error while logging in");
            e.printStackTrace();
        }
    }
}
