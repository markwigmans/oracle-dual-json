package com.btb.odj.service;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.activemq.ArtemisContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.OracleContainer;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
public class TestContainersConfig {

    @Bean
    @ServiceConnection
    public ArtemisContainer artemisContainer() {
        return new ArtemisContainer("apache/activemq-artemis:2.35.0");
    }

    @Bean
    @ServiceConnection
    public OracleContainer oracleContainer() {
        return new OracleContainer("gvenzl/oracle-xe");
    }

    @Bean
    @ServiceConnection
    public MongoDBContainer mongoDBContainer() {
        return new MongoDBContainer(DockerImageName.parse("mongo:7"));
    }

    @Bean
    @ServiceConnection
    public ElasticsearchContainer elasticsearchContainer() {
        ElasticsearchContainer container = new ElasticsearchContainer("elasticsearch:8.14.3");
        container.getEnvMap().put("discovery.type", "single-node");
        container.getEnvMap().put("xpack.security.enabled", "false");
        return container;
    }
}
