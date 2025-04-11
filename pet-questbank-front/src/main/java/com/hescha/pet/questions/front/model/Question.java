package com.hescha.pet.questions.front.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question extends AbstractEntity {
    private String question;
    private String answer;
}

