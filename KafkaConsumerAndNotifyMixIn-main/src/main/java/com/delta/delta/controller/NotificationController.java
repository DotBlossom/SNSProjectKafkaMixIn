package com.delta.delta.controller;

import com.delta.delta.DTO.PreferenceDataDto;

import com.delta.delta.service.DataAggToPreferenceService;
import com.delta.delta.service.NotificationService;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@RestController
@RequiredArgsConstructor
@RequestMapping("/kafkaListener")
public class NotificationController {
    Logger log = LoggerFactory.getLogger(NotificationController.class);
    private final NotificationService notificationService;
    private final DataAggToPreferenceService dataAggToPreferenceService;

    @GetMapping(value = "/subscribe/{user_id}", produces = "text/event-stream;charset=UTF-8")
    public SseEmitter subscribe(@PathVariable(value = "user_id") Long userId,
                                @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        log.info("lastId {}" ,lastEventId);
        return notificationService.subscribe(userId.toString(), lastEventId);
    }

    @GetMapping("/preference/posts")
    public ResponseEntity<PreferenceDataDto> getPreferencePostList() {
        return ResponseEntity.ok(dataAggToPreferenceService.sendPreferData());

    }


}