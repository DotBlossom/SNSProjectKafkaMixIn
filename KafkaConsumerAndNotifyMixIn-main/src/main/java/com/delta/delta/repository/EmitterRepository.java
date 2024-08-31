package com.delta.delta.repository;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.yaml.snakeyaml.emitter.Emitter;

import java.util.List;
import java.util.Map;

public interface EmitterRepository {
    SseEmitter save(String emitterId, SseEmitter sseEmitter);
    void saveEventCache(String emitterId, Object event);
    //void saveLogOutEventCache(String eventCacheId, Object event);
    Map<String, SseEmitter> findAllEmitterStartWithByReceiverId(String receiverId);
    Map<String, Object> findAllEventCacheStartWithByReceiverId(String receiverId);
    //Map<String, Object> findAllLogOutEventCacheStartWithByReceiverId(String receiverId);
    void deleteById(String id);
    void deleteAllEmitterStartWithId(String receiverId);
    void deleteAllEventCacheStartWithId(String receiverId);

    Map<String, SseEmitter> findAllEmitters();

}
