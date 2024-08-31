package com.delta.delta.repository;

import com.delta.delta.entity.Notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;


public interface NotificationRepository extends JpaRepository<Notification, Long> {
    void deleteById(@NonNull Long id);
    List<Notification> findAllById(Long id);

}
