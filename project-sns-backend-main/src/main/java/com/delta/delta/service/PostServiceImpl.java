package com.delta.delta.service;

import com.delta.delta.entity.Post;
import com.delta.delta.entity.User;
import com.delta.delta.repository.PostRepository;
import com.delta.delta.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public Post createPost(Long userId, String content) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Post post = Post.builder()
                    .user(user)
                    .content(content)
                    .build();
            return postRepository.save(post);
        } else {
            throw new RuntimeException("해당 ID의 유저를 찾지 못했습니다: " + userId);
        }
    }

    @Override
    public List<Post> getPostsByUserId(Long userId) {
        return postRepository.findByUser_UserId(userId);
    }

    @Override
    public Optional<Post> getPostById(Long postId) {
        return postRepository.findById(postId);
    }

    @Override
    @Transactional
    public Post updatePost(Long postId, String newContent) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            post.setContent(newContent);
            return postRepository.save(post);
        } else {
            throw new RuntimeException("해당 ID의 포스트를 찾지 못했습니다: " + postId);
        }
    }

    @Override
    @Transactional
    public void deletePost(Long postId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isPresent()) {
            postRepository.delete(postOptional.get());
        } else {
            throw new RuntimeException("해당 ID의 포스트를 찾지 못했습니다: " + postId);
        }
    }
}
