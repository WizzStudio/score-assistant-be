package com.wz.score_assistant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ScoreAssistantApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScoreAssistantApplication.class, args);
    }

}
