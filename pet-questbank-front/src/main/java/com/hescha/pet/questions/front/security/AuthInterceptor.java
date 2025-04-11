package com.hescha.pet.questions.front.security;

import com.hescha.pet.questions.front.service.TokenStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements ClientHttpRequestInterceptor {

    private final TokenStorage tokenStorage; // наш сервис хранения токена

    @Override
    public ClientHttpResponse intercept(HttpRequest request,
                                        byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {

        String accessToken = tokenStorage.getAccessToken();
        if (accessToken != null && !accessToken.isBlank()) {
            request.getHeaders().set("Authorization", "Bearer " + accessToken);
        }
        return execution.execute(request, body);
    }
}
