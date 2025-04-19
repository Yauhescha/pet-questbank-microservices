package com.hescha.pet.questions.front.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Topic extends AbstractEntity{
    private String name;
    @JsonManagedReference
    private List<Question> questions = new ArrayList<>();

    @Override
    public String toString() {
        return name;
    }
}
