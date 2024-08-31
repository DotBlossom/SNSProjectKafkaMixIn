package com.delta.delta.service;

import com.delta.delta.entity.Comment;
import com.delta.delta.entity.Post;
import com.delta.delta.entity.User;
import com.delta.delta.repository.CommentRepository;
import com.delta.delta.repository.PostRepository;
import com.delta.delta.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public Comment createComment(Long postId, Long userId, String content) {
        Optional<Post> postOptional = postRepository.findById(postId);
        Optional<User> userOptional = userRepository.findById(userId);

        if (postOptional.isPresent() && userOptional.isPresent()) {
            Post post = postOptional.get();
            User user = userOptional.get();
            Comment comment = Comment.builder()
                    .post(post)
                    .user(user)
                    .content(content)
                    .build();
            return commentRepository.save(comment);
        } else {
            throw new RuntimeException("해당 ID의 포스팅 또는 유저를 찾지 못했습니다: postId=" + postId + ", userId=" + userId);
        }
    }

    @Override
    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPost_PostId(postId);
    }

    @Override
    public List<Comment> getCommentsByUserId(Long userId) {
        return commentRepository.findByUser_UserId(userId);
    }

    @Override
    public Optional<Comment> getCommentById(Long commentId) {
        return commentRepository.findById(commentId);
    }

    @Override
    @Transactional
    public Comment updateComment(Long commentId, String newContent) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if (commentOptional.isPresent()) {
            Comment comment = commentOptional.get();
            comment.setContent(newContent);
            return commentRepository.save(comment);
        } else {
            throw new RuntimeException("해당 ID의 댓글을 찾지 못했습니다: " + commentId);
        }
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if (commentOptional.isPresent()) {
            commentRepository.delete(commentOptional.get());
        } else {
            throw new RuntimeException("해당 ID의 댓글을 찾지 못했습니다: " + commentId);
        }
    }
}
