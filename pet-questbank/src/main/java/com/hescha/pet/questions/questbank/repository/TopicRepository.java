package com.hescha.pet.questions.questbank.repository;

import com.hescha.pet.questions.questbank.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, Long> {
}

