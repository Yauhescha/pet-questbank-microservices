package com.hescha.pet.questions.questbank.repository;

import com.hescha.pet.questions.questbank.model.Question;
import com.hescha.pet.questions.questbank.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByQuestion(String name);
    List<Question> findByTopic(Topic topic);
}
