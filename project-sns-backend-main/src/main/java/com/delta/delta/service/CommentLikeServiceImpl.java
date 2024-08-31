package com.delta.delta.service;

import com.delta.delta.entity.CommentLike;
import com.delta.delta.repository.CommentLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentLikeServiceImpl implements CommentLikeService {

    @Autowired
    private CommentLikeRepository CommentLikeRepository;

    @Override
    public Optional<CommentLike> findByUserIdAndCommentId(Long userId, Long commentId) {
        return CommentLikeRepository.findByUser_UserIdAndComment_CommentId(userId, commentId);
    }

    @Override
    public Long countByCommentId(Long commentId) {
        return CommentLikeRepository.countByComment_CommentId(commentId);
    }

    @Override
    public List<CommentLike> findAllByUserId(Long userId) {
        return CommentLikeRepository.findAllByUser_UserId(userId);
    }

    @Override
    public CommentLike saveCommentLike(CommentLike CommentLike) {
        return CommentLikeRepository.save(CommentLike);
    }

    @Override
    public void deleteCommentLike(Long userCommentLikeId) {
        CommentLikeRepository.deleteById(userCommentLikeId);
    }
}
