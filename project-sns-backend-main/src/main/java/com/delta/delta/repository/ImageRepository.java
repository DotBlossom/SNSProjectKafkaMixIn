package com.delta.delta.repository;

import com.delta.delta.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findByPost_PostId(Long postId); // 특정 게시물의 모든 이미지 반환
}
