package com.hescha.pet.questions.questbank.controller;

import com.hescha.pet.questions.questbank.model.Topic;
import com.hescha.pet.questions.questbank.service.QuestionService;
import com.hescha.pet.questions.questbank.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/topic")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService service;
    private final QuestionService questionService;

    @GetMapping
    public ResponseEntity<List<Topic>> readAll() {
        return ResponseEntity.ok(service.readAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Topic> read(@PathVariable("id") Long id) {
        Topic topic = service.read(id);
        topic.setQuestions(questionService.findByTopic(topic));
        return ResponseEntity.ok(topic);
    }

    @PostMapping
    public ResponseEntity<Topic> save(@RequestBody Topic entity) {
        return ResponseEntity.ok(service.create(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Topic> update(@PathVariable Long id, @RequestBody Topic entity) {
        return ResponseEntity.ok(service.update(id, entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
