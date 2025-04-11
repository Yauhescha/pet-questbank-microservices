package com.hescha.pet.questions.front.controller;


import com.hescha.pet.questions.front.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final RestTemplate restTemplate;

    @Value("${backend.url}/user")
    private String userApiUrl;

    @GetMapping
    public String listUsers(Model model) {
        ResponseEntity<User[]> response =
                restTemplate.getForEntity(userApiUrl, User[].class);
        List<User> users = Arrays.asList(response.getBody());
        model.addAttribute("users", users);
        return "users/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("user", new User());
        return "users/create";
    }

    @PostMapping
    public String createUser(@ModelAttribute("user") User user) {
        restTemplate.postForEntity(userApiUrl, user, User.class);
        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        User user = restTemplate.getForObject(userApiUrl + "/" + id, User.class);
        model.addAttribute("user", user);
        return "users/edit";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable Long id,
                             @ModelAttribute("user") User user) {
        restTemplate.put(userApiUrl + "/" + id, user);
        return "redirect:/users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        restTemplate.delete(userApiUrl + "/" + id);
        return "redirect:/users";
    }

    @GetMapping("/{id}")
    public String viewUser(@PathVariable Long id, Model model) {
        User user = restTemplate.getForObject(userApiUrl + "/" + id, User.class);
        model.addAttribute("user", user);
        return "users/view";
    }
}
