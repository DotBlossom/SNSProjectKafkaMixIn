package com.delta.delta.service;

import com.delta.delta.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ImageService {

    Image createImage(Long postId, MultipartFile file, String uniqueFileName);

    List<Image> getImagesByPostId(Long postId);

    Optional<Image> getImageById(Long imageId);

    void deleteImage(Long imageId);
}
