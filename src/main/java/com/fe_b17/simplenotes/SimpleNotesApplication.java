package com.fe_b17.simplenotes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SimpleNotesApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleNotesApplication.class, args);
    }

}
