package com.delta.delta.controller;

import com.delta.delta.entity.CommentLike;
import com.delta.delta.service.CommentLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/commentlike")
public class CommentLikeController {

    @Autowired
    private CommentLikeService commentLikeService;

    // 특정 사용자 + 댓글에 대한 좋아요 조회
    @GetMapping("/user/{userId}/comment/{commentId}")
    public ResponseEntity<Optional<CommentLike>> getCommentLike(@PathVariable Long userId, @PathVariable Long commentId) {
        Optional<CommentLike> commentLike = commentLikeService.findByUserIdAndCommentId(userId, commentId);
        if (commentLike.isPresent()) {
            return ResponseEntity.ok(commentLike);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 특정 댓글에 대한 좋아요 수 조회
    @GetMapping("/comment/{commentId}/count")
    public ResponseEntity<Long> countCommentLikes(@PathVariable Long commentId) {
        Long count = commentLikeService.countByCommentId(commentId);
        return ResponseEntity.ok(count);
    }

    // 특정 사용자가 좋아요한 댓글 목록 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CommentLike>> getCommentLikesByUser(@PathVariable Long userId) {
        List<CommentLike> commentLikes = commentLikeService.findAllByUserId(userId);
        return ResponseEntity.ok(commentLikes);
    }

    // 댓글에 좋아요 추가
    @PostMapping
    public ResponseEntity<CommentLike> likeComment(@RequestBody CommentLike commentLike) {
        CommentLike savedCommentLike = commentLikeService.saveCommentLike(commentLike);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCommentLike);
    }

    // 댓글에 좋아요 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> unlikeComment(@PathVariable Long id) {
        commentLikeService.deleteCommentLike(id);
        return ResponseEntity.noContent().build();
    }
}
