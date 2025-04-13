package com.hescha.pet.questions.front.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Topic extends AbstractEntity{
    private String name;

    @Override
    public String toString() {
        return name;
    }
}
