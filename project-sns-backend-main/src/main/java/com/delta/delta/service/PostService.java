package com.delta.delta.service;

import com.delta.delta.entity.Post;

import java.util.List;
import java.util.Optional;

public interface PostService {

    // 새로운 포스트 작성 메서드
    Post createPost(Long userId, String content);

    // userId 의 모든 포스트 get 메서드
    List<Post> getPostsByUserId(Long userId);

    // postId 로 특정 포스트 get 메서드
    Optional<Post> getPostById(Long postId);

    // 포스트 업데이트 메서드
    Post updatePost(Long postId, String newContent);

    // postId 로 포스트 삭제 메서드
    void deletePost(Long postId);
}
