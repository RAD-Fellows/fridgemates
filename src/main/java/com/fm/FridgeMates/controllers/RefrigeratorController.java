package com.fm.FridgeMates.controllers;

import com.fm.FridgeMates.models.ApplicationUser;
import com.fm.FridgeMates.models.Comment;
import com.fm.FridgeMates.models.Ingredient;
import com.fm.FridgeMates.models.Refrigerator;
import com.fm.FridgeMates.repositories.ApplicationUserRepository;
import com.fm.FridgeMates.repositories.CommentRepository;
import com.fm.FridgeMates.repositories.IngredientRepository;
import com.fm.FridgeMates.repositories.RefrigeratorRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
        List<Comment> topLevelBrowsingUserComments = new ArrayList<>();
        List<Comment> displayComments = new ArrayList<>();

        if (browsingUser != null) {
            for (Comment comment : allComments) {
                if (comment.getCommentCreator().getId().equals(browsingUser.getId()) && comment.getParentComment() == null) {
                    topLevelBrowsingUserComments.add(comment);
                }
            }
        }

        // Reset the list to include only top-level comments from the current browsing user
        topLevelBrowsingUserComments.clear();
        for (Comment comment : allComments) {
            if (comment.getCommentCreator().getId().equals(browsingUser.getId()) && comment.getParentComment() == null) {
                topLevelBrowsingUserComments.add(comment);
            }
        }

        // If browsingUser owns the refrigerator, display all comments
        if (browsingUser != null && browsingUser.getId().equals(foundUser.getId())) {
            displayComments.addAll(allComments);
        }
        // Otherwise, display browsingUser's top-level comments and their children
        else if (!topLevelBrowsingUserComments.isEmpty()) {
            for (Comment comment : allComments) {
                if ((comment.getParentComment() == null && comment.getCommentCreator().getId().equals(browsingUser.getId())) || topLevelBrowsingUserComments.contains(comment.getParentComment())) {
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

    @DeleteMapping("/ingredient/{id}")
    public RedirectView deleteIngredient(@PathVariable Long id, Principal p) {

        ingredientRepository.deleteById(id);

        String browsingUserUsername = p.getName();
        ApplicationUser browsingUser = applicationUserRepository.findByUsername(browsingUserUsername);
        if (browsingUser != null) {
            return new RedirectView("/refrigerator/" + browsingUser.getId());
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

    @DeleteMapping("/delete-comment/{commentId}")
    public RedirectView deleteComment(@PathVariable Long commentId, Principal p) {
        commentRepository.deleteById(commentId);

        String browsingUserUsername = p.getName();
        ApplicationUser browsingUser = applicationUserRepository.findByUsername(browsingUserUsername);
        if (browsingUser != null) {
            return new RedirectView("/refrigerator/" + browsingUser.getId());
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
