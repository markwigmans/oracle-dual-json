package com.btb.odj.service;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.OracleContainer;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.utility.DockerImageName;

public class TestContainers {

    public static final OracleContainer oracleContainer;
    public static final MongoDBContainer mongoDBContainer;
    public static final GenericContainer<?> redisContainer;
    public static final ElasticsearchContainer elasticsearchContainer;

    static {
        oracleContainer = new OracleContainer(DockerImageName.parse("gvenzl/oracle-free:23-slim-faststart")
                        .asCompatibleSubstituteFor("gvenzl/oracle-xe"))
                .withDatabaseName("FREEPDB1")
                .withUsername("testuser")
                .withPassword("testpassword");

        mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:7"));

        redisContainer = new GenericContainer<>(DockerImageName.parse("redis:7")).withExposedPorts(6379);

        elasticsearchContainer = new ElasticsearchContainer(
                DockerImageName.parse("docker.elastic.co/elasticsearch/elasticsearch:7.17.9"));

        // Start all containers
        oracleContainer.start();
        mongoDBContainer.start();
        redisContainer.start();
        elasticsearchContainer.start();
    }

    public static void registerProperties(DynamicPropertyRegistry registry) {
        // Oracle properties
        registry.add("spring.datasource.url", () -> oracleContainer.getJdbcUrl().replace("ORCLPDB1", "FREEPDB1"));
        registry.add("spring.datasource.username", oracleContainer::getUsername);
        registry.add("spring.datasource.password", oracleContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", oracleContainer::getDriverClassName);

        // MongoDB properties
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);

        // Redis properties
        registry.add("spring.redis.host", redisContainer::getHost);
        registry.add("spring.redis.port", () -> redisContainer.getMappedPort(6379));

        // Elasticsearch properties
        registry.add("spring.elasticsearch.uris", elasticsearchContainer::getHttpHostAddress);
    }
}
