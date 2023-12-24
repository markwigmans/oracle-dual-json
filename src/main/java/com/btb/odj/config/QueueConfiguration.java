package com.btb.odj.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@ConfigurationProperties(prefix = "queue")
public class QueueConfiguration {
    private final String UpdateDataTopic = "race-data.topic";
}
