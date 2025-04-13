package com.hescha.pet.questions.questbank.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question extends AbstractEntity {
    @Column
    private String question;
    @Column(length = 20480)
    private String answer;

    // Много вопросов -> одна тема
    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @Override
    public String toString() {
        return "Question{" +
                "question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                ", topic=" + (topic == null? "none" : topic.getName()) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Question question1 = (Question) o;
        return Objects.equals(question, question1.question) && Objects.equals(answer, question1.answer) && Objects.equals(topic, question1.topic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), question, answer, topic);
    }
}

