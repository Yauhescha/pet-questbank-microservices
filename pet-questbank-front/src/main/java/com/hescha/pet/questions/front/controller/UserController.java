package com.hescha.pet.questions.front.controller;


import com.hescha.pet.questions.front.model.User;
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
public class UserController {

    private final RestTemplate restTemplate;

    @Value("${backend.url.users:http://localhost:1234/users}")
    private String userApiUrl;

    public UserController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // READ (список)
    @GetMapping
    public String getAllUsers(Model model) {
        ResponseEntity<User[]> response =
                restTemplate.getForEntity(userApiUrl, User[].class);
        List<User> users = Arrays.asList(response.getBody());
        model.addAttribute("users", users);
        return "users/list";
    }

    // CREATE - показать форму
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        return "users/create";
    }

    // CREATE - сохранить
    @PostMapping
    public String createUser(@ModelAttribute("user") User user) {
        restTemplate.postForEntity(userApiUrl, user, User.class);
        return "redirect:/users";
    }

    // UPDATE - форма
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        User user = restTemplate.getForObject(userApiUrl + "/" + id, User.class);
        model.addAttribute("user", user);
        return "users/edit";
    }

    // UPDATE - сохранить
    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable Long id,
                             @ModelAttribute("user") User user) {
        restTemplate.put(userApiUrl + "/" + id, user);
        return "redirect:/users";
    }

    // DELETE
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        restTemplate.delete(userApiUrl + "/" + id);
        return "redirect:/users";
    }

    // VIEW (детальная страница)
    @GetMapping("/{id}")
    public String viewUser(@PathVariable Long id, Model model) {
        User user = restTemplate.getForObject(userApiUrl + "/" + id, User.class);
        model.addAttribute("user", user);
        return "users/view";
    }
}
