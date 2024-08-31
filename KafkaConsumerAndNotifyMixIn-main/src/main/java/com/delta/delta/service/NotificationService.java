package com.delta.delta.service;

import com.delta.delta.DTO.NotificationsDto;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {
    void consumeObject(NotificationsDto dto);
    SseEmitter subscribe(String userId, String lastEventId);
    void sendToClient(SseEmitter emitter, String emitterId, Object data);
    void deleteIsReadNotification(Long id);
}
