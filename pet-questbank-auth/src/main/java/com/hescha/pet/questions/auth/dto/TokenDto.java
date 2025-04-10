package com.hescha.pet.questions.auth.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class TokenDto {
    private String accessToken;
    private String refreshToken;
    private Date accessExpiresIn;
    private Date refreshExpiresIn;
}
