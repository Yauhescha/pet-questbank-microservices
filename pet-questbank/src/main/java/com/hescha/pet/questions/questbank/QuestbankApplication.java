package com.hescha.pet.questions.questbank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class QuestbankApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuestbankApplication.class, args);
	}

}
