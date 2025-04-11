package com.hescha.pet.questions.front.controller;

import com.hescha.pet.questions.front.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    // ======= LOGIN =======
    @GetMapping("/login")
    public String loginForm() {
        return "auth/login"; // login.html
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username,
                          @RequestParam String password,
                          Model model) {
        boolean success = authService.login(username, password);
        if (!success) {
            model.addAttribute("error", "Неверные учетные данные или ошибка сервера");
            return "auth/login";
        }
        return "redirect:/";
    }

    // ======= REGISTRATION =======
    @GetMapping("/registration")
    public String registrationForm() {
        return "auth/registration"; // registration.html
    }

    @PostMapping("/registration")
    public String doRegistration(@RequestParam String username,
                                 @RequestParam String password,
                                 Model model) {
        boolean success = authService.register(username, password);
        if (!success) {
            model.addAttribute("error", "Ошибка при регистрации (логин уже существует или сервер недоступен)");
            return "auth/registration";
        }
        return "redirect:/";
    }

    // ======= REFRESH =======
    @GetMapping("/refresh")
    public String refreshToken() {
        authService.refreshToken();
        return "redirect:/";
    }

    // ======= LOGOUT =======
    @GetMapping("/logout")
    public String logout() {
        authService.logout();
        return "redirect:/";
    }
}

