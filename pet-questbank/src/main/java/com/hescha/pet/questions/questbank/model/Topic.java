package com.hescha.pet.questions.questbank.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id",
        scope      = Topic.class
)
public class Topic extends AbstractEntity{
    @Column(nullable = false, unique = true)
    private String name;

    @Transient
    private List<Question> questions = new ArrayList<>();

    @Override
    public String toString() {
        return name;
    }
}
