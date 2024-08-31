package com.delta.delta.repository;

import com.delta.delta.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUser_UserId(Long userId); //사용자의 게시물들 반환. 반환할 때 정렬 로직 추가하면 바로 사용할 수 있을듯
}
