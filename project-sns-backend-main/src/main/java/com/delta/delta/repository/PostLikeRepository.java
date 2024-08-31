package com.delta.delta.repository;

import com.delta.delta.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    List<PostLike> findByPost_PostId(Long postId); // 특정 게시물의 모든 좋아요 반환
    List<PostLike> findByUser_UserId(Long userId); // 특정 사용자의 모든 좋아요 반환
    boolean existsByPost_PostIdAndUser_UserId(Long postId, Long userId); //사용자가 게시물에 좋아요 여부
}
