package com.delta.delta.service;

import com.delta.delta.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    // 새로운 댓글 작성 메서드
    Comment createComment(Long postId, Long userId, String content);

    // 특정 게시물의 모든 댓글 get 메서드
    List<Comment> getCommentsByPostId(Long postId);

    // 특정 사용자의 모든 댓글 get 메서드
    List<Comment> getCommentsByUserId(Long userId);

    // commentId 로 특정 댓글 get 메서드
    Optional<Comment> getCommentById(Long commentId);

    // 댓글 업데이트 메서드
    Comment updateComment(Long commentId, String newContent);

    // commentId 로 댓글 삭제 메서드
    void deleteComment(Long commentId);
}
