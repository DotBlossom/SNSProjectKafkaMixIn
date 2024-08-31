package com.delta.delta.repository;

import com.delta.delta.entity.Notification;
import com.delta.delta.entity.NotificationStack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationStackRepository extends JpaRepository<NotificationStack, Long> {
    NotificationStack findByOwnerId(Long ownerId);
    NotificationStack findByNotifications_id(Long id);
}
