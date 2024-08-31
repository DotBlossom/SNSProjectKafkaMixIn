package com.delta.delta.repository;

import com.delta.delta.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 특정 게시물의 모든 댓글 반환
    List<Comment> findByPost_PostId(Long postId);

    // 특정 사용자의 모든 댓글 반환
    List<Comment> findByUser_UserId(Long userId);
}