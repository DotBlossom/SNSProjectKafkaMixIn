package com.delta.delta.repository;

import com.delta.delta.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    // 특정 사용자와 댓글에 대한 좋아요가 존재하는지 확인하는 메서드
    Optional<CommentLike> findByUser_UserIdAndComment_CommentId(Long userId, Long commentId);

    // 특정 댓글에 대한 좋아요 수를 계산하는 메서드
    Long countByComment_CommentId(Long commentId);

    // 특정 사용자가 좋아요한 댓글들의 리스트를 가져오는 메서드
    List<CommentLike> findAllByUser_UserId(Long userId);
}
