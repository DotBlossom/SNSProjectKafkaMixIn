package com.delta.delta.repository;

import com.delta.delta.DTO.PreferenceDataDto;
import com.delta.delta.entity.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;
@RequiredArgsConstructor
@Repository
public class DataAggToPreferenceRepositoryImpl implements DataAggToPreferenceRepository{

    public PreferenceDataDto prefer;
    public final NotificationRepository notificationRepository;

    public boolean save(HashMap<Long, Long> hashMap){

        Map<Long, Long> top20 = hashMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(20)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));


        prefer = new PreferenceDataDto();
        prefer.setPostIdToPrefer(new ArrayList<>(top20.keySet()));
        prefer.setEventType("prefer");


        //List<Long> postIds = prefer.getPostIdToPrefer(); // getPostIdToPrefer() 메서드가 Long 타입의 ID 리스트를 반환한다고 가정
        //List<Notification> notifications = notificationRepository.findAllById(postIds);
        //List<String> fileNames = notifications.stream()
        //        .map(notification -> notification.getFileNameFirst() != null ? notification.getFileNameFirst() : "none")
        //        .toList();
        //prefer.setPostFileNames(fileNames);
        hashMap.clear();

        return true;

    }

    public PreferenceDataDto sendPreferData(){
        return prefer;
    }
}
