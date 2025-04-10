package com.hescha.pet.questions.questbank.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question extends AbstractEntity {
    @Column
    private String question;
    @Column(length = 20480)
    private String answer;
}

