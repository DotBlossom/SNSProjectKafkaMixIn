package com.delta.delta.controller;


import com.delta.delta.DTO.NotificationsDto;

import com.delta.delta.service.KafkaMessagePublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/kafka/producer")
public class NotificationsEventController {

    @Autowired
    private KafkaMessagePublisher publisher;

    @PostMapping("/notifications/{userId}")
    public void sendObject(@PathVariable Long userId, @RequestBody NotificationsDto dto) {
        publisher.sendObjectToTopic(userId, dto);
        // type import or message making
    }


    // not used now
    @GetMapping("/{message}")
    public ResponseEntity<?> publishMessage(@PathVariable String message) {
        try {
            publisher.sendMessageToTopic(message);

            return ResponseEntity.ok("message published successfully ..");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
}