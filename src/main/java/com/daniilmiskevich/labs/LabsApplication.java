package com.daniilmiskevich.labs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class LabsApplication {

    public static void main(String[] args) {
        SpringApplication.run(LabsApplication.class, args);
    }

}
