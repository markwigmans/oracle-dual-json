package com.btb.odj.config;

import com.github.javafaker.Faker;
import java.util.Locale;
import java.util.Random;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public Faker createFaker() {
        // Dutch texts with fixed seed
        return new Faker(new Locale("nl-NL"), new Random(24));
    }
}
