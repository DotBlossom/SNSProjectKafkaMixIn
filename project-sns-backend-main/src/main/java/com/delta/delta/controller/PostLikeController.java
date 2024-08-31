package com.delta.delta.controller;

import com.delta.delta.entity.PostLike;
import com.delta.delta.service.PostLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/postlikes")
public class PostLikeController {

    @Autowired
    private PostLikeService postLikeService;

    // 새로운 좋아요 추가
    @PostMapping
    public ResponseEntity<PostLike> likePost(@RequestParam Long postId, @RequestParam Long userId) {
        PostLike postLike = postLikeService.likePost(postId, userId);
        return ResponseEntity.ok(postLike);
    }

    // 좋아요 삭제
    @DeleteMapping("/{postLikeId}")
    public ResponseEntity<Void> unlikePost(@PathVariable Long postLikeId) {
        postLikeService.unlikePost(postLikeId);
        return ResponseEntity.noContent().build();
    }

    // postId 게시물의 모든 좋아요 조회
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<PostLike>> getLikesByPostId(@PathVariable Long postId) {
        List<PostLike> postLikes = postLikeService.getLikesByPostId(postId);
        return ResponseEntity.ok(postLikes);
    }

    // userId 사용자의 모든 좋아요 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostLike>> getLikesByUserId(@PathVariable Long userId) {
        List<PostLike> postLikes = postLikeService.getLikesByUserId(userId);
        return ResponseEntity.ok(postLikes);
    }

    // 특정 좋아요 조회 << 이거 필요할지는 잘 모르겠습니다.
    @GetMapping("/{postLikeId}")
    public ResponseEntity<PostLike> getPostLikeById(@PathVariable Long postLikeId) {
        Optional<PostLike> postLike = postLikeService.getPostLikeById(postLikeId);
        return postLike.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
