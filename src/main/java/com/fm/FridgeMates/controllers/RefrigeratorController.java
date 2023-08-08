package com.fm.FridgeMates.controllers;

import com.fm.FridgeMates.models.ApplicationUser;
import com.fm.FridgeMates.models.Comment;
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
import java.util.ArrayList;
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
        ApplicationUser browsingUser = null;
        if (p != null) {
            String username = p.getName();
            browsingUser = applicationUserRepository.findByUsername(username);
        }
        m.addAttribute("browsingUser", browsingUser);

        ApplicationUser foundUser = applicationUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        m.addAttribute("foundUser", foundUser);

        List<Comment> allComments = foundUser.getRefrigerator().getComments();
        List<Comment> browsingUserComments = new ArrayList<>();
        List<Comment> displayComments = new ArrayList<>();

        if (browsingUser != null) {
            for (Comment comment : allComments) {
                if (comment.getCommentCreator().getId().equals(browsingUser.getId())) {
                    browsingUserComments.add(comment);
                }
            }
        }

        // If browsingUser owns the refrigerator, display all comments
        if (browsingUser != null && browsingUser.getId().equals(foundUser.getId())) {
            displayComments.addAll(allComments);
        }
        // Otherwise, display browsingUser's comments and their children
        else if (!browsingUserComments.isEmpty()) {
            for (Comment comment : allComments) {
                if (comment.getParentComment() == null || browsingUserComments.contains(comment.getParentComment())) {
                    displayComments.add(comment);
                }
            }
        }

        m.addAttribute("comments", displayComments);
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

    @PostMapping("/refrigerator/add-comment")
    public RedirectView addComment(Principal p, String body, Long refrigeratorId, Long foundUserId){

        if (p != null){
            String username = p.getName();
            ApplicationUser browsingUser = applicationUserRepository.findByUsername(username);
            LocalDate createdAt = LocalDate.now();
            Refrigerator refrigerator = refrigeratorRepository.findById(refrigeratorId)
                    .orElseThrow(() -> new ResourceNotFoundException("Refrigerator not found"));
            Comment parentComment = null;
            Comment newComment = new Comment(browsingUser, createdAt, body, false, false, refrigerator, parentComment);
            commentRepository.save(newComment);
            refrigerator.getComments().add(newComment);
            refrigeratorRepository.save(refrigerator);
            return new RedirectView("/refrigerator/" + foundUserId);
        }
        return new RedirectView("/");
    }

    @PostMapping("/refrigerator/add-reply")
    public RedirectView addReply(Principal p, String body, Long refrigeratorId, Long foundUserId, Long parentCommentId){
        if (p != null){
            String username = p.getName();
            ApplicationUser browsingUser = applicationUserRepository.findByUsername(username);
            LocalDate createdAt = LocalDate.now();
            Refrigerator refrigerator = refrigeratorRepository.findById(refrigeratorId)
                    .orElseThrow(() -> new ResourceNotFoundException("Refrigerator not found"));
            Comment parentComment = null;
            if(parentCommentId !=null){
                parentComment = commentRepository.findById(parentCommentId)
                        .orElseThrow(() -> new ResourceNotFoundException("Parent Comment Not Found"));
            }

            Comment newComment = new Comment(browsingUser, createdAt, body, false, false, refrigerator, parentComment);
            commentRepository.save(newComment);
            refrigerator.getComments().add(newComment);
            refrigeratorRepository.save(refrigerator);
            return new RedirectView("/refrigerator/" + foundUserId);
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
