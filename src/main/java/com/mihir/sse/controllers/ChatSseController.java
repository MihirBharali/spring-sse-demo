package com.mihir.sse.controllers;

import com.mihir.sse.domain.Dialogue;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Controller
public class ChatSseController {

    private static final Logger LOGGER = Logger.getLogger(ChatSseController.class);

    public final ApplicationEventPublisher eventPublisher;

    public ChatSseController(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }


    private final Map<String, CopyOnWriteArrayList<SseEmitter>> emitters = new ConcurrentHashMap<>();

    @GetMapping("/conversation/{chatId}")
    public SseEmitter handle(@PathVariable String chatId) {

        SseEmitter emitter = new SseEmitter(300_000L);
        CopyOnWriteArrayList<SseEmitter> emitterList = emitters.get(chatId);

        if(emitterList == null) {
            emitterList = new CopyOnWriteArrayList<>();
        }
        emitterList.add(emitter);
        this.emitters.put(chatId, emitterList);
        return emitter;
    }



    @RequestMapping("start/{chatId}/{speaker}/{message}")
    public void startPublishing(@PathVariable String chatId,
                                @PathVariable String speaker,
                                @PathVariable String message) {
        Dialogue dialogue= new Dialogue(chatId, speaker, message);
        this.eventPublisher.publishEvent(dialogue);

    }


    @EventListener
    public void onDailogue(Dialogue dialogue) {

        String chatId = dialogue.getChatId();

        CopyOnWriteArrayList<SseEmitter> emitterList = emitters.get(chatId);

        List<SseEmitter> deadEmitters = new ArrayList<>();

        emitterList.forEach(emitter -> {
            try {
                emitter.send(dialogue);
            }
            catch (Exception e) {
                deadEmitters.add(emitter);
            }
        });

        this.emitters.remove(deadEmitters);
    }
}