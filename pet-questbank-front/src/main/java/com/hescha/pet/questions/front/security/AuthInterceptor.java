package com.hescha.pet.questions.front.security;

import com.hescha.pet.questions.front.exception.UnauthenticatedException;
import com.hescha.pet.questions.front.service.AuthService;
import com.hescha.pet.questions.front.service.TokenStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.http.client.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements ClientHttpRequestInterceptor {

    private final TokenStorage tokenStorage;
    private final AuthService authService;

    @Override
    public ClientHttpResponse intercept(
            HttpRequest request,
            byte[] body,
            ClientHttpRequestExecution execution
    ) throws IOException {

        // 1) Установим заголовок Authorization, если есть accessToken
        String accessToken = tokenStorage.getAccessToken();
        if (accessToken != null && !accessToken.isBlank()) {
            request.getHeaders().set("Authorization", "Bearer " + accessToken);
        }

        // 2) Делаем запрос
        ClientHttpResponse response = execution.execute(request, body);

        // 3) Проверяем статус
        if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            response.close(); // Закрываем текущее тело ответа

            // 3.1) Если accessToken == null (или пуст), значит пользователь не авторизован
            if (accessToken == null || accessToken.isBlank()) {
                throw new UnauthenticatedException("Пользователь не авторизован");
            } else {
                // 3.2) Пытаемся рефрешить
                boolean refreshed = authService.refreshToken();
                if (refreshed) {
                    // Повторный запрос
                    String newToken = tokenStorage.getAccessToken();
                    if (newToken != null && !newToken.isBlank()) {
                        request.getHeaders().set("Authorization", "Bearer " + newToken);
                    }
                    response = execution.execute(request, body);
                    // Если снова 401 - отдаём наверх
                } else {
                    // Рефреш не удался - снова бросаем исключение
                    throw new UnauthenticatedException("Пользователь не авторизован (refresh не удался)");
                }
            }
        }

        return response;
    }
}
