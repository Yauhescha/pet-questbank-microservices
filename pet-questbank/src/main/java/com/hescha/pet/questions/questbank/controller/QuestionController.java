package com.hescha.pet.questions.questbank.controller;

import com.hescha.pet.questions.questbank.model.Question;
import com.hescha.pet.questions.questbank.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService service;

    @GetMapping
    public ResponseEntity<List<Question>> readAll() {
        return ResponseEntity.ok(service.readAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Question> read(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.read(id));
    }

    @PostMapping
    public ResponseEntity<Question> save(@RequestBody Question entity) {
        return ResponseEntity.ok(service.create(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Question> update(@PathVariable Long id, @RequestBody Question entity) {
        return ResponseEntity.ok(service.update(id, entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
