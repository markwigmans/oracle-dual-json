package com.btb.odj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.btb.odj.repository.jpa")
@EnableMongoRepositories(basePackages = "com.btb.odj.repository.mongodb")
@EnableElasticsearchRepositories(basePackages = "com.btb.odj.repository.elasticsearch")
@EnableCaching
public class OracleDualJsonApplication {

    public static void main(String[] args) {
        SpringApplication.run(OracleDualJsonApplication.class, args);
    }
}
