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
public class Topic extends AbstractEntity{
    @Column(nullable = false, unique = true)
    private String name;

    @Override
    public String toString() {
        return name;
    }
}
