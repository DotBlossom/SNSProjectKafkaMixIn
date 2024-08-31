package com.delta.delta.service;

import com.delta.delta.entity.Post;
import com.delta.delta.entity.PostLike;
import com.delta.delta.entity.User;
import com.delta.delta.repository.PostLikeRepository;
import com.delta.delta.repository.PostRepository;
import com.delta.delta.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PostLikeServiceImpl implements PostLikeService {

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public PostLike likePost(Long postId, Long userId) {
        if (postLikeRepository.existsByPost_PostIdAndUser_UserId(postId, userId)) {
            throw new RuntimeException("사용자가 이미 해당 게시물에 좋아요를 눌렀습니다: postId=" + postId + ", userId=" + userId);
        }

        Optional<Post> postOptional = postRepository.findById(postId);
        Optional<User> userOptional = userRepository.findById(userId);

        if (postOptional.isPresent() && userOptional.isPresent()) {
            Post post = postOptional.get();
            User user = userOptional.get();
            PostLike postLike = PostLike.builder()
                    .post(post)
                    .user(user)
                    .build();
            return postLikeRepository.save(postLike);
        } else {
            throw new RuntimeException("해당 ID의 포스팅 또는 유저를 찾지 못했습니다: postId=" + postId + ", userId=" + userId);
        }
    }

    @Override
    @Transactional
    public Long unlikePost(Long postLikeId) {
        Optional<PostLike> postLikeOptional = postLikeRepository.findById(postLikeId);

        if (postLikeOptional.isPresent()) {
            PostLike target = postLikeOptional.get();
            Long postId = target.getPost().getPostId();

            postLikeRepository.delete(target);

            return postId;
        } else {
            throw new RuntimeException("해당 ID의 좋아요를 찾지 못했습니다: " + postLikeId);
        }
    }

    @Override
    public List<PostLike> getLikesByPostId(Long postId) {
        return postLikeRepository.findByPost_PostId(postId);
    }

    @Override
    public Optional<PostLike> getPostLikeById(Long postLikeId) {
        return postLikeRepository.findById(postLikeId);
    }

    @Override
    public List<PostLike> getLikesByUserId(Long userId) {
        return postLikeRepository.findByUser_UserId(userId);
    }
}


