package com.delta.delta.service;

import com.delta.delta.DTO.NotificationsDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class KafkaMessagePublisher {

    @Autowired
    private KafkaTemplate<String,Object> template;

    @Value("${notifications.topic-name}")
    String topicName = "";

    public void sendObjectToTopic(Long userId, NotificationsDto dto) {
        try {
            String key = String.valueOf(userId % 3);

            Headers headers = new RecordHeaders();
            headers.add("eventType-header", dto.getEventType().getBytes());

            ProducerRecord<String, Object> record = new ProducerRecord<>(topicName, key, dto);

            for (Header header : headers) { record.headers().add(header); }

            CompletableFuture<SendResult<String, Object>> future =
                    template.send(record);




            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    RecordMetadata recordMetadata = result.getRecordMetadata();
                    sendLog(dto.toString(), recordMetadata);

                } else {
                    System.out.println("Unable to send message=[" +
                            dto.toString() + "] due to : " + ex.getMessage());
                }
            });

        } catch (Exception ex) {
            System.out.println("ERROR : "+ ex.getMessage());
        }
    }

    public void sendMessageToTopic(String message){
        CompletableFuture<SendResult<String, Object>> future =
                template.send(topicName, message);
        future.whenComplete((result,ex)->{
            if (ex == null) {
                RecordMetadata recordMetadata = result.getRecordMetadata();
                sendLog(message, recordMetadata);
            } else {
                System.out.println("Unable to send message=[" +
                        message + "] due to : " + ex.getMessage());
            }
        });
    }

    private static void sendLog(String message, RecordMetadata recordMetadata) {
        log.info("Sent message = {} with offset = {}", message, recordMetadata.offset());
        log.info("Topic Name = {}", recordMetadata.topic());
        log.info("Topic Partition Count = {}", recordMetadata.partition());
    }
}