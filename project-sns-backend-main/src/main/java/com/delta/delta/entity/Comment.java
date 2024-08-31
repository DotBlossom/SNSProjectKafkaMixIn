package com.delta.delta.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    @JsonIgnoreProperties({"comments", "postLikes", "images"})
    private Post post;


    // comment 부분은 POST 요청으로 requestbody로 처리하는 부분이 없고,
    // 렌더링 할 user 정보가 필요한데, elementProps로 구성된 follow 객체는 ignore가 안됨

    // 만약 comment 에 Requestbody POST 요청이 들어온다면, Include에 해당하는 이 직렬데이터를 넣어야함
    // 추후 서비스 구체화 협의 후 코드를 결정하겠습니다.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIncludeProperties({"userId", "profileImage", "username", "createdAt"})
    private User user;

    @Column(columnDefinition = "VARCHAR(200)", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    @JsonIgnoreProperties("childComments")
    private Comment parentComment;

    @CreationTimestamp
    @Column(name = "created_at", columnDefinition = "TIMESTAMP", updatable = false)
    private LocalDateTime createdAt;

    @CreationTimestamp
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedAt;
}
