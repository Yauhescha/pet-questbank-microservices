package com.hescha.pet.questions.front.controller;

import com.hescha.pet.questions.front.model.Question;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/questions")
public class QuestionController {

    private final RestTemplate restTemplate;

    @Value("${backend.url.questions:http://localhost:1234/questions}")
    private String questionApiUrl;

    public QuestionController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public String getAllQuestions(Model model) {
        ResponseEntity<Question[]> response = restTemplate.getForEntity(questionApiUrl, Question[].class);
        List<Question> questions = Arrays.asList(response.getBody());
        model.addAttribute("questions", questions);
        return "questions/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("question", new Question());
        return "questions/create";
    }

    @PostMapping
    public String createQuestion(@ModelAttribute("question") Question question) {
        restTemplate.postForEntity(questionApiUrl, question, Question.class);
        return "redirect:/questions";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Question question = restTemplate.getForObject(questionApiUrl + "/" + id, Question.class);
        model.addAttribute("question", question);
        return "questions/edit";
    }

    @PostMapping("/update/{id}")
    public String updateQuestion(@PathVariable Long id,
                                 @ModelAttribute("question") Question question) {
        restTemplate.put(questionApiUrl + "/" + id, question);
        return "redirect:/questions";
    }

    @GetMapping("/delete/{id}")
    public String deleteQuestion(@PathVariable Long id) {
        restTemplate.delete(questionApiUrl + "/" + id);
        return "redirect:/questions";
    }

    @GetMapping("/{id}")
    public String viewQuestion(@PathVariable Long id, Model model) {
        Question question = restTemplate.getForObject(questionApiUrl + "/" + id, Question.class);
        model.addAttribute("question", question);
        return "questions/view";
    }
}
