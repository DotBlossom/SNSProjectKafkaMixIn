package com.delta.delta.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

// user가 notification 할당이 안되있으면 생성
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationStack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long ownerId;

    @Column(nullable = false)
    private int stackLength;



    @OneToMany(mappedBy = "notificationStack", orphanRemoval = true)
    @JsonIgnoreProperties("notificationStack")
    private List<Notification> notifications = new ArrayList<>();



}
