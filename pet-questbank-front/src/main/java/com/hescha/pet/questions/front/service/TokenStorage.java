package com.hescha.pet.questions.front.service;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import lombok.Data;

// Будем хранить токен в session scope
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Data
public class TokenStorage {

    private String accessToken;
    private String refreshToken;
}
