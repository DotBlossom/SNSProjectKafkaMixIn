package com.delta.delta.service;

import com.delta.delta.DTO.NotificationsDto;
import com.delta.delta.DTO.PreferenceDataDto;
import com.delta.delta.repository.DataAggToPreferenceRepository;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;

import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class DataAggToPreferenceServiceImpl implements DataAggToPreferenceService {

    Logger log = LoggerFactory.getLogger(DataAggToPreferenceServiceImpl.class);

    private final DataAggToPreferenceRepository dataAggToPreferenceRepository;
    private final Map<Long, Long> unitTimePostPreferenceChecker = new ConcurrentHashMap<>();

    private MessageListenerContainer listenerContainer;
    private final KafkaListenerEndpointRegistry registry;

    @KafkaListener(topics = "${notifications.topic-name}", groupId = "${notifications.group-id-2}",
            id ="dataAggContainer")
    public void consumeAgg(NotificationsDto dto) {
        //get info msg

        if (dto.getEventType().equals("postLike")) {
            unitTimePostPreferenceChecker.compute(dto.getPostId(), (key, oldValue) -> (oldValue == null) ? 1L : oldValue + 1L);
            log.info("logged DataAggs {} ", dto.toString());
        } else if (dto.getEventType().equals("postUnlike")) {
            unitTimePostPreferenceChecker.compute(dto.getPostId(), (key, oldValue) -> (oldValue == null) ? -1L : oldValue - 1L);
            log.info("logged unliked {} ", dto.toString());
        }


    }

    public void stopListener() {
        listenerContainer = registry.getListenerContainer("dataAggContainer");
        //assert listenerContainer != null;
        listenerContainer.stop();
    }

    public void startListener() {
        listenerContainer = registry.getListenerContainer("dataAggContainer");
        //assert listenerContainer != null;
        listenerContainer.start();
    }

    //@Scheduled(fixedRate = 60 * 60 * 1000) // container 잠시 리슨 중지 후 , 재게
    public void sendDataToRepo() {
        // 리스너 일시 중지
        if (registry.getListenerContainer("dataAggContainer") == null) {
            log.info("logged repoErrors");
            return;
        }
        log.info("logged repoOn");
        stopListener();

        HashMap<Long, Long> hashMap = new HashMap<>(unitTimePostPreferenceChecker);

        // Repository로 데이터 전송
        dataAggToPreferenceRepository.save(hashMap);
        // 다시 카운트하기
        unitTimePostPreferenceChecker.clear();
        // 리스너 재개
        startListener();
    }

    public PreferenceDataDto sendPreferData(){
        sendDataToRepo();
        return dataAggToPreferenceRepository.sendPreferData();
    }
}
