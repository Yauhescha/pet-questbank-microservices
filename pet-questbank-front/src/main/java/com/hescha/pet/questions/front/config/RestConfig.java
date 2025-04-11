package com.hescha.pet.questions.front.config;


import com.hescha.pet.questions.front.security.AuthInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
public class RestConfig {

    @Bean
    public RestTemplate restTemplate(AuthInterceptor authInterceptor) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(List.of(authInterceptor));
        return restTemplate;
    }
}
