package com.delta.delta.service;

import com.delta.delta.entity.PostLike;

import java.util.List;
import java.util.Optional;

public interface PostLikeService {

    PostLike likePost(Long postId, Long userId);

    Long unlikePost(Long postLikeId);

    List<PostLike> getLikesByPostId(Long postId);

    Optional<PostLike> getPostLikeById(Long postLikeId);

    List<PostLike> getLikesByUserId(Long userId);
}
