package com.delta.delta.service;

import com.delta.delta.entity.CommentLike;

import java.util.List;
import java.util.Optional;

public interface CommentLikeService {

    // 특정 사용자와 댓글에 대한 좋아요를 찾는 메서드
    Optional<CommentLike> findByUserIdAndCommentId(Long userId, Long commentId);

    // 특정 댓글에 대한 좋아요 수를 계산하는 메서드
    Long countByCommentId(Long commentId);

    // 특정 사용자가 좋아요한 댓글들을 가져오는 메서드
    List<CommentLike> findAllByUserId(Long userId);

    // 댓글에 좋아요를 추가하는 메서드
    CommentLike saveCommentLike(CommentLike CommentLike);

    // 댓글에 좋아요를 삭제하는 메서드
    void deleteCommentLike(Long userCommentLikeId);
}
