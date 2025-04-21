package com.hescha.pet.questions.questbank.service;

import com.hescha.pet.questions.questbank.model.Question;
import com.hescha.pet.questions.questbank.model.Topic;
import com.hescha.pet.questions.questbank.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService extends CrudService<Question> {

    private final QuestionRepository repository;

    public QuestionService(QuestionRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public List<Question> findByQuestion(String name) {
        return repository.findByQuestion(name);
    }


    public Question update(Long id, Question entity) {
        Question read = read(id);
        if (read == null) {
            throw new RuntimeException("Entity Question not found");
        }
        updateFields(entity, read);
        return update(read);

    }

    private void updateFields(Question entity, Question read) {
        read.setQuestion(entity.getQuestion());
        read.setAnswer(entity.getAnswer());
        read.setMarked(entity.getMarked());
        read.setTopic(entity.getTopic());
    }

    public List<Question> findByTopic(Topic topic) {
        return repository.findByTopic(topic);
    }
}
