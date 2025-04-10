package com.hescha.pet.questions.auth.controller;

import com.hescha.pet.questions.auth.model.User;
import com.hescha.pet.questions.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService service;

    @GetMapping
    public ResponseEntity<List<User>> readAll() {
        return ResponseEntity.ok(service.readAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> read(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.read(id));
    }

    @PostMapping
    public ResponseEntity<User> save(@ModelAttribute("entity") User entity) {
        User saved;
        if (entity.getId() == null) {
            saved = service.create(entity);
        } else {
            saved = service.update(entity.getId(), entity);
        }
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
