package com.tts.TechTalentTwitter.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration //while autoscanning a file this will know to create
public class WebMvcConfiguration implements WebMvcConfigurer {
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = 
            new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }
}