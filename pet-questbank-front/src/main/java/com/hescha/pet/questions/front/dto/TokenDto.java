package com.hescha.pet.questions.front.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {
    private String accessToken;
    private String refreshToken;
    private Date accessExpiresIn;
    private Date refreshExpiresIn;
}
