package com.delta.delta.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

//Using sse method
@Repository
public class EmitterRepositoryImpl implements EmitterRepository {
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<String, Object> eventCache = new ConcurrentHashMap<>();
    //private final Map<String, Object> logOutEventCache = new ConcurrentHashMap<>();

    @Override
    public SseEmitter save(String emitterId, SseEmitter sseEmitter) {
        emitters.put(emitterId, sseEmitter);
        return sseEmitter;
    }

    @Override
    public void saveEventCache(String eventCacheId, Object event) {
        eventCache.put(eventCacheId, event);
    }



    @Override
    public Map<String, SseEmitter> findAllEmitters() {
        return new HashMap<>(emitters);
    }

    @Override
    public Map<String, SseEmitter> findAllEmitterStartWithByReceiverId(String receiverId) {
        return emitters.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(receiverId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Map<String, Object> findAllEventCacheStartWithByReceiverId(String receiverId) {
        return eventCache.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(receiverId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }


    @Override
    public void deleteById(String id) {
        emitters.remove(id);
    }

    @Override
    public void deleteAllEmitterStartWithId(String receiverId) {
        emitters.forEach(
                (key, emitter) -> {
                    if (key.startsWith(receiverId)) {
                        emitters.remove(key);
                    }
                }
        );
    }

    @Override
    public void deleteAllEventCacheStartWithId(String receiverId) {
        eventCache.forEach(
                (key, emitter) -> {
                    if (key.startsWith(receiverId)) {
                        eventCache.remove(key);
                    }
                }
        );
    }
}
