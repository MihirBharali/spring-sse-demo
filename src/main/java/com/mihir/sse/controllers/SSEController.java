package com.mihir.sse.controllers;

import com.mihir.sse.domain.MemoryInfo;
import com.mihir.sse.domain.Student;
import com.mihir.sse.processors.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Controller
public class SSEController {

    @Autowired
    private RedisService redisService;

    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<SseEmitter>();

    @GetMapping("/memory")
    public SseEmitter handle() {

        SseEmitter emitter = new SseEmitter();
        this.emitters.add(emitter);

        emitter.onCompletion(() -> this.emitters.remove(emitter));
        emitter.onTimeout(() -> this.emitters.remove(emitter));

        return emitter;
    }



    @GetMapping("/save/{id}/{name}/{gender}/{grade}")
    @ResponseBody
    public String save(@PathVariable String id,
                       @PathVariable String name,
                       @PathVariable String gender,
                       @PathVariable int grade) {

        try {
            Student student = new Student(name, gender, id, grade);
            redisService.saveStudent(student);
        } catch (IllegalArgumentException iae) {
            return "FAILURE : Invalid data";
        }
        return "SUCCESS";
    }
    @GetMapping("/get/{id}")
    @ResponseBody
    public Student get(@PathVariable String id) {
        return redisService.getStudent(id);
    }


    @EventListener
    public void onMemoryInfo(MemoryInfo memoryInfo) {
        List<SseEmitter> deadEmitters = new ArrayList<>();
        this.emitters.forEach(emitter -> {
            try {
                emitter.send(memoryInfo);
            }
            catch (Exception e) {
                deadEmitters.add(emitter);
            }
        });

        this.emitters.remove(deadEmitters);
    }
}