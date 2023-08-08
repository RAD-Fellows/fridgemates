package com.fm.FridgeMates.repositories;

import com.fm.FridgeMates.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
