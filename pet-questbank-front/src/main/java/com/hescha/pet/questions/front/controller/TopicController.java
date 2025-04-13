package com.hescha.pet.questions.front.controller;

import com.hescha.pet.questions.front.model.Topic;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/topics")
@RequiredArgsConstructor
public class TopicController {

    private final RestTemplate restTemplate;

    @Value("${backend.url}/topic")
    private String topicApiUrl;

    @GetMapping
    public String listTopics(Model model) {
        ResponseEntity<Topic[]> response =
                restTemplate.getForEntity(topicApiUrl, Topic[].class);
        List<Topic> topics = Arrays.asList(response.getBody());
        model.addAttribute("topics", topics);
        return "topics/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("topic", new Topic());
        return "topics/create";
    }

    @PostMapping
    public String createTopic(@ModelAttribute("topic") Topic topic) {
        restTemplate.postForEntity(topicApiUrl, topic, Topic.class);
        return "redirect:/topics";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Topic topic = restTemplate.getForObject(topicApiUrl + "/" + id, Topic.class);
        model.addAttribute("topic", topic);
        return "topics/edit";
    }

    @PostMapping("/update/{id}")
    public String updateTopic(@PathVariable Long id,
                                 @ModelAttribute("topic") Topic topic) {
        restTemplate.put(topicApiUrl + "/" + id, topic);
        return "redirect:/topics";
    }

    @GetMapping("/delete/{id}")
    public String deleteTopic(@PathVariable Long id) {
        restTemplate.delete(topicApiUrl + "/" + id);
        return "redirect:/topics";
    }

    @GetMapping("/{id}")
    public String viewTopic(@PathVariable Long id, Model model) {
        Topic topic = restTemplate.getForObject(topicApiUrl + "/" + id, Topic.class);
        model.addAttribute("topic", topic);
        return "topics/view";
    }
}
