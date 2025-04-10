package com.hescha.pet.questions.questbank.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;


@Data
@MappedSuperclass
public abstract class AbstractEntity {
    @Id
    @GeneratedValue(generator = "increment")
    protected Long id;
}
