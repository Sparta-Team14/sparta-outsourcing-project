package com.example.jeogiyoproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class JeogiyoProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(JeogiyoProjectApplication.class, args);
    }

}
