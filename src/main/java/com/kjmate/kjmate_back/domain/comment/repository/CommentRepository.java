package com.kjmate.kjmate_back.domain.comment.repository;

import com.kjmate.kjmate_back.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
