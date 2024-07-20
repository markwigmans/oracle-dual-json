package com.btb.odj;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.info.GitProperties;
import org.springframework.context.event.EventListener;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.btb.odj.repository.jpa")
@EnableMongoRepositories(basePackages = "com.btb.odj.repository.mongodb")
@EnableElasticsearchRepositories(basePackages = "com.btb.odj.repository.elasticsearch")
@Slf4j
@RequiredArgsConstructor
public class OracleDualJsonApplication {

    private final GitProperties gitProperties;

    public static void main(String[] args) {
        SpringApplication.run(OracleDualJsonApplication.class, args);
    }

    @EventListener(ApplicationStartedEvent.class)
    public void logGitInfo() {
        log.info("Application started with Git commit: '{}'", gitProperties.getShortCommitId());
    }
}
