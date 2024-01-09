package com.btb.odj.config;

import com.github.javafaker.Faker;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class ApplicationConfig {

    @Value("${data.threads:0}")
    private int threads;

    @Bean
    public Faker createFaker() {
        // Dutch texts with fixed seed
        return new Faker(new Locale("nl-NL"), new Random(24));
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ExecutorService executors() {
        if (threads <= 0) {
            threads = Runtime.getRuntime().availableProcessors();
        }
        return Executors.newWorkStealingPool(threads);
    }
}
