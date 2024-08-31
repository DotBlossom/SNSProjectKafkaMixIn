package com.delta.delta.controller;

import com.delta.delta.entity.Post;
import com.delta.delta.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    // 새로운 포스트 생성
    @PostMapping
    public ResponseEntity<Post> createPost(@RequestParam Long userId, @RequestParam String content) {
        Post post = postService.createPost(userId, content);
        return ResponseEntity.ok(post);
    }

    // userId 의 모든 포스트 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Post>> getPostsByUserId(@PathVariable Long userId) {
        List<Post> posts = postService.getPostsByUserId(userId);
        return ResponseEntity.ok(posts);
    }

    // postId 로 포스트 조회
    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPostById(@PathVariable Long postId) {
        Optional<Post> post = postService.getPostById(postId);
        return post.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // postId 포스트 업데이트
    @PutMapping("/{postId}")
    public ResponseEntity<Post> updatePost(@PathVariable Long postId, @RequestParam String content) {
        Post updatedPost = postService.updatePost(postId, content);
        return ResponseEntity.ok(updatedPost);
    }

    // postId 포스트 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }
}
