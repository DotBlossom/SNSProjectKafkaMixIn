package com.delta.delta.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "postlike")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_post_like_id")
    private Long userPostLikeId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    @JsonIgnoreProperties({"comments", "postLikes", "images"})
    private Post post;

    // postLike 부분은 POST 요청을 requestbody로 처리하는 부분이 없고, (이러면 include 문제 X)
    // 인스타그램 UI 상으로는 좋아요 목록을 보면 user 기본정보가 보여서 include로 구성
    // elementProps로 구성된 follow 객체는 ignore가 안되서, 차집합으로 include로 사용

    // 만약 postLike 에 Requestbody POST 요청이 들어온다면, Include한 이 직렬데이터를 넣어야함
    // 추후 서비스 구체화 협의 후 코드를 결정하겠습니다.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIncludeProperties({"userId", "profileImage", "username", "createdAt"})
    private User user;

    @CreationTimestamp
    @Column(name = "created_at", columnDefinition = "TIMESTAMP", updatable = false)
    private LocalDateTime createdAt;
}
