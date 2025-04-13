package com.hescha.pet.questions.questbank.service;

import com.hescha.pet.questions.questbank.model.Topic;

import com.hescha.pet.questions.questbank.repository.TopicRepository;
import org.springframework.stereotype.Service;

@Service
public class TopicService extends CrudService<Topic> {

    private final TopicRepository repository;

    public TopicService(TopicRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public Topic update(Long id, Topic entity) {
        Topic read = read(id);
        if (read == null) {
            throw new RuntimeException("Entity Topic not found");
        }
        updateFields(entity, read);
        return update(read);

    }

    private void updateFields(Topic entity, Topic read) {
        read.setName(entity.getName());
    }
}
