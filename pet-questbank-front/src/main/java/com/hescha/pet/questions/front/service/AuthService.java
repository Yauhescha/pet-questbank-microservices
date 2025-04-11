package com.hescha.pet.questions.front.service;


import com.hescha.pet.questions.front.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final RestTemplate restTemplate;
    private final TokenStorage tokenStorage;

    @Value("${backend.url}")
    private String backendUrl;

    public boolean login(String username, String password) {
        String url = backendUrl + "/auth/login" +
                "?username=" + username + "&password=" + password;
        try {
            ResponseEntity<TokenDto> response =
                    restTemplate.postForEntity(url, null, TokenDto.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                TokenDto body = response.getBody();
                saveTokens(body);
                return true;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    public boolean register(String username, String password) {
        String url = backendUrl + "/auth/registration" +
                "?username=" + username + "&password=" + password;
        try {
            ResponseEntity<TokenDto> response =
                    restTemplate.postForEntity(url, null, TokenDto.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                saveTokens(response.getBody());
                return true;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    public boolean refreshToken() {
        String refreshToken = tokenStorage.getRefreshToken();
        if (refreshToken == null) {
            return false;
        }
        String url = backendUrl + "/auth/refresh" + "?refreshToken=" + refreshToken;
        try {
            ResponseEntity<TokenDto> response =
                    restTemplate.postForEntity(url, null, TokenDto.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                saveTokens(response.getBody());
                return true;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    private void saveTokens(TokenDto dto) {
        if (dto != null) {
            tokenStorage.setAccessToken(dto.getAccessToken());
            tokenStorage.setRefreshToken(dto.getRefreshToken());
        }
    }

    public String getCurrentUsername() {
        try {
            String url = backendUrl + "/auth/user";
            // Возвращает String (username)
            ResponseEntity<String> response =
                    restTemplate.getForEntity(url, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    public boolean isLoggedIn() {
        return tokenStorage.getAccessToken() != null;
    }

    public String getAccessToken() {
        return tokenStorage.getAccessToken();
    }

    public void logout() {
        tokenStorage.setAccessToken(null);
        tokenStorage.setRefreshToken(null);
    }
}
