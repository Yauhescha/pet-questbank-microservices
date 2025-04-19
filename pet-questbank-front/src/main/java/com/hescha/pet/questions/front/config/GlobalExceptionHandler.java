package com.hescha.pet.questions.front.config;

import com.hescha.pet.questions.front.exception.UnauthenticatedException;
import com.hescha.pet.questions.front.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final AuthService authService;

    @ExceptionHandler(UnauthenticatedException.class)
    public String handleUnauthenticated() {
        // Просто возвращаем редирект на страницу логина
        return "redirect:/auth/login";
    }

    @ModelAttribute
    public void addCommonAttributes(Model model) {
        if (authService.isLoggedIn()) {
            model.addAttribute("currentUser", authService.getCurrentUsername());
        } else {
            model.addAttribute("currentUser", null);
        }
    }
}
