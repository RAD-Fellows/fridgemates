package com.fm.FridgeMates.models;

import com.fasterxml.jackson.core.SerializableString;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Comment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    ApplicationUser commentCreator;
    LocalDate date;
    String body;
    Boolean accepted;
    Boolean completed;
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment parentComment;
    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> replies = new ArrayList<>();

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

    public Comment(ApplicationUser commentCreator, LocalDate date, String body, Boolean accepted, Boolean completed, Refrigerator refrigerator, Comment parentComment) {
        this.commentCreator = commentCreator;
        this.date = date;
        this.body = body;
        this.accepted = accepted;
        this.completed = completed;
        this.refrigerator = refrigerator;
        this.parentComment = parentComment;
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

    public Comment getParentComment() {
        return parentComment;
    }

    public List<Comment> getReplies() {
        return replies;
    }

    public void setReplies(List<Comment> replies) {
        this.replies = replies;
    }

    public void setParentComment(Comment parentComment) {
        this.parentComment = parentComment;
    }

    public void addReply(Comment reply) {
        replies.add(reply);
        reply.setParentComment(this);
    }

    public void removeReply(Comment reply) {
        replies.remove(reply);
        reply.setParentComment(null);
    }

    public Refrigerator getRefrigerator() {
        return refrigerator;
    }

    public void setRefrigerator(Refrigerator refrigerator) {
        this.refrigerator = refrigerator;
    }
}
