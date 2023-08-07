package com.fm.FridgeMates.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    ApplicationUser commentCreator;

    LocalDate date;

    String body;

    Boolean accepted;

    Boolean completed;

    @ManyToOne
    Refrigerator refrigerator;

    public Comment() {
    }

    public Comment(ApplicationUser commentCreator, LocalDate date, String body, Boolean accepted, Boolean completed, Refrigerator refrigerator) {
        this.commentCreator = commentCreator;
        this.date = date;
        this.body = body;
        this.accepted = accepted;
        this.completed = completed;
        this.refrigerator = refrigerator;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ApplicationUser getCommentCreator() {
        return commentCreator;
    }

    public void setCommentCreator(ApplicationUser commentCreator) {
        this.commentCreator = commentCreator;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Refrigerator getRefrigerator() {
        return refrigerator;
    }

    public void setRefrigerator(Refrigerator refrigerator) {
        this.refrigerator = refrigerator;
    }
}
