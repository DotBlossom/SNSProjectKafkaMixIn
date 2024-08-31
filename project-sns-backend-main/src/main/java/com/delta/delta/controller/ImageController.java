package com.delta.delta.controller;

import com.delta.delta.entity.Image;
import com.delta.delta.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    // 파일이 저장될 경로
    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    // 새로운 이미지 생성
    @PostMapping
    public ResponseEntity<String> createImage(@RequestParam Long postId, @RequestParam("fileName") MultipartFile file) {
        try {
            // 디렉토리 존재 여부 확인 및 생성
            File directory = new File(UPLOAD_DIR);
            if (!directory.exists()) {
                directory.mkdirs(); // 디렉토리 생성
            }

            // 파일명과 저장 경로 설정 (중복 방지)
            String originalFileName = file.getOriginalFilename();
            String uniqueFileName = System.currentTimeMillis() + "_" + originalFileName;
            String savedFilePath = UPLOAD_DIR + uniqueFileName;

            // 로그 추가: 저장 경로와 파일명 확인
            System.out.println("Saving file to: " + savedFilePath);

            // 파일을 서버에 저장 (스트림 사용)
            File savedFile = new File(savedFilePath);
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, savedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            // 파일 경로 또는 이름과 다른 필요한 정보를 사용하여 Image 엔티티 생성
            Image image = imageService.createImage(postId, file, uniqueFileName);

            // 클라이언트에게 파일 접근 경로를 반환
            String fileAccessUrl = "/uploads/" + uniqueFileName;
            return ResponseEntity.ok(fileAccessUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("파일 저장 중 오류가 발생했습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("서버 내부 오류가 발생했습니다.");
        }
    }

    // postId 게시물의 모든 이미지 조회
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<Image>> getImagesByPostId(@PathVariable Long postId) {
        List<Image> images = imageService.getImagesByPostId(postId);
        System.out.println("Number of images returned: " + images.size());
        return ResponseEntity.ok(images);

    }

    // imageId 이미지 조회
    @GetMapping("/{imageId}")
    public ResponseEntity<Image> getImageById(@PathVariable Long imageId) {
        Optional<Image> image = imageService.getImageById(imageId);
        return image.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // imageId 이미지 삭제
    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long imageId) {
        imageService.deleteImage(imageId);
        return ResponseEntity.noContent().build();
    }
}
