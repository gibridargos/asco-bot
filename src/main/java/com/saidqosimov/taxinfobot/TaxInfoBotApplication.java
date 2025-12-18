package com.saidqosimov.taxinfobot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TaxInfoBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaxInfoBotApplication.class, args);
    }

}
