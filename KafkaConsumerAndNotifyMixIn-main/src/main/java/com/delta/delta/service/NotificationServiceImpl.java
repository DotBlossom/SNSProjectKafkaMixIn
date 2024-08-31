package com.delta.delta.service;

import com.delta.delta.DTO.NotificationsDto;
import com.delta.delta.entity.Notification;
import com.delta.delta.entity.NotificationStack;
import com.delta.delta.repository.EmitterRepository;

import com.delta.delta.repository.NotificationRepository;
import com.delta.delta.repository.NotificationStackRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    //chore: logIn 이후, react에서 불러오면 이거 만들어줘야함 notification;; + cnt ++

    private final EmitterRepository emitterRepository;
    private final NotificationStackRepository notificationStackRepository;
    private final NotificationRepository notificationRepository;

    private static final long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private static final int MAX_NOTIFICATIONS_COUNT = 10;
    private final EntityManager em;
    Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @KafkaListener(topics = "${notifications.topic-name}", groupId = "${notifications.group-id-1}")
    @Transactional
    public void consumeObject(NotificationsDto dto) {


        log.info("get aleat {}" , dto.toString());
        if (dto.getEventType().equals("postUnlike")) {
            return ;
        }


        Long receiverId = dto.getReceiverId();
        String emitterReceiverId = receiverId.toString();

        if (dto.getIsRead() != null) {
            if (dto.getIsRead().equals("Y")) {
                NotificationStack stack = notificationStackRepository.findByOwnerId(receiverId);


                List<Notification> targets = stack.getNotifications();
                targets.stream()
                        .filter(notification -> notification.getIsRead().equals("Y"))
                        .forEach(filteredNotification -> notificationRepository.deleteById(filteredNotification.getId()));


                return;
            }
        }


        Notification notification = Notification.builder()
                .senderId(dto.getSenderId())
                .receiverId(dto.getReceiverId())
                .eventCreatedTime(LocalDateTime.now())
                .eventType(dto.getEventType())
                .postId(dto.getPostId())
                .senderName(dto.getSenderName())
                .isSent("N")
                //.fileNameFirst(dto.getPostFileName())
                .build();
        Notification pNotification = notificationRepository.save(notification);

        NotificationStack stack = notificationStackRepository.findByOwnerId(receiverId);
        // 여기서 delete 오면 삭제함.


        if (stack != null) {
            int len = stack.getStackLength();



            if (len >= MAX_NOTIFICATIONS_COUNT) {
                Notification target = stack.getNotifications().get(0);
                notificationRepository.deleteById(target.getId());
                stack.setStackLength(len - 1);
            }



            stack.setStackLength(len + 1);
            pNotification.setNotificationStack(notificationStackRepository.save(stack));


        } else {

            log.info("logged {} ", notification.getId());
            NotificationStack newStack = NotificationStack.builder()
                    .ownerId(dto.getReceiverId())
                    .stackLength(1)
                    .build();

            List<Notification> LN = new ArrayList<>();
            LN.add(notification);
            newStack.setNotifications(LN);

            pNotification.setNotificationStack(notificationStackRepository.save(newStack));

        }
        // update,, query낭비..? dynamicUpdateAnnotaion
        em.flush();


        // emitter가 여려개 연결된 경우? notifi 말고 확장 or 페이지 여려개
        // kafka listen -> notification 저장 -> eventSend(sendToClient)
        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllEmitterStartWithByReceiverId(emitterReceiverId);
        if (!sseEmitters.isEmpty()) {
            sseEmitters.forEach(
                    (key, emitter) -> {
                        emitterRepository.saveEventCache(key, pNotification);
                        sendToClient(emitter, key, pNotification);
                        pNotification.setIsSent("Y");
                    }
            );

        } else {
            //logout push
            emitterRepository.saveEventCache(emitterReceiverId, pNotification);
        }

    }


    public void sendToClient(SseEmitter emitter, String emitterId, Object data) {
        try {

            if (data.equals("connected!")) {
                emitter.send(SseEmitter.event()
                        .id(emitterId)
                        .data(data)
                );

            } else {
                emitter.send(SseEmitter.event()
                        .id(emitterId)
                        .name("notification")
                        .data(data)

                );
            }

            log.info("Kafka로 부터 전달 받은 메세지 전송. emitterId : {}, message : {}", emitterId, data);
        } catch (IOException e) {
            emitterRepository.deleteById(emitterId);
            log.error("메시지 전송 에러 : {}", e);
        }
    }

    public SseEmitter subscribe(String userId, String lastEventId) {
        String emitterId = userId + "_" + System.currentTimeMillis();
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));
        log.info("emitterId : {} 사용자 emitter 연결 ", emitterId);

        emitter.onCompletion(() -> {
            log.info("onCompletion callback");
            emitterRepository.deleteById(emitterId);
        });
        emitter.onTimeout(() -> {
            log.info("onTimeout callback");
            emitterRepository.deleteById(emitterId);
        });

        sendToClient(emitter, emitterId, "connected!"); // 503 에러방지 더미 데이터


        //send clinet 미수신 data -> 다시 보내주기.
        if (!lastEventId.isEmpty()) {
            Map<String, Object> events = emitterRepository.findAllEventCacheStartWithByReceiverId(userId);
            NotificationStack stack =  notificationStackRepository.findByOwnerId(Long.valueOf(userId));
            List<Notification> sentNotifications = stack.getNotifications();

            sentNotifications.forEach(filteredNotification -> sendToClient(emitter, emitterId, filteredNotification));

        }

        return emitter;
    }

    public void deleteIsReadNotification(Long id) {
        // if front emit trigger -> deleteById run
    }
    @Scheduled(fixedRate = 60 * 1000 * 5) //heartbeat 메세지 전달.
    public void sendHeartbeat() {
        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllEmitters();
        sseEmitters.forEach((key, emitter) -> {
            try {
                emitter.send(SseEmitter.event().id(key).name("heartbeat").data(""));
            } catch (IOException e) {
                emitterRepository.deleteById(key);
                log.error("heartBeat is failed: {}", e.getMessage());
            }
        });
    }
}
