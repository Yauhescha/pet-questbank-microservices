package com.hescha.pet.questions.front.controller;

import com.hescha.pet.questions.front.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class HomeController {

    private final AuthService authService;

    @GetMapping("/")
    public String homePage(Model model) {
        if (authService.isLoggedIn()) {
            // Проверим, как зовут текущего пользователя
            String username = authService.getCurrentUsername();
            model.addAttribute("currentUser", username);
        } else {
            model.addAttribute("currentUser", null);
        }
        return "index"; // index.html
    }
}
