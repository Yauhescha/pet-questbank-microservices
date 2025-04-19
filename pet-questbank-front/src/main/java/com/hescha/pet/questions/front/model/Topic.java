package com.hescha.pet.questions.front.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id",
        scope      = Topic.class
)
public class Topic extends AbstractEntity{
    private String name;
    private List<Question> questions = new ArrayList<>();

    @Override
    public String toString() {
        return name;
    }
}
