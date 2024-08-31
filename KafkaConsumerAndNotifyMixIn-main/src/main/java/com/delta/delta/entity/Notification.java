package com.delta.delta.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;


import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long senderId;
    private Long receiverId;

    private Long postId;

    @Column(nullable = false)
    private String eventType;

    private LocalDateTime eventCreatedTime;
    //private String fileNameFirst;
    private String isSent;
    private String isRead;
    private String senderName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_stack")
    private NotificationStack notificationStack;


}
