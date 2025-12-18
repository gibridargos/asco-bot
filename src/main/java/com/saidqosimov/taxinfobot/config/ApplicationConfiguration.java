package com.saidqosimov.taxinfobot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class ApplicationConfiguration {
    @Value("${bot.name}")
    String username;
    @Value("${bot.token}")
    String token;
    @Value("${bot.notification-photo}")
    String photoUrl;
}
