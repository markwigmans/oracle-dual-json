package com.btb.odj.service;

import static java.util.Optional.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.btb.odj.model.Data_Race;
import com.btb.odj.repository.jpa.DataRaceRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.OracleContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
class DataRaceServiceTest {

    @Container
    static OracleContainer oracleContainer = new OracleContainer("gvenzl/oracle-xe");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", oracleContainer::getJdbcUrl);
        registry.add("spring.datasource.username", oracleContainer::getUsername);
        registry.add("spring.datasource.password", oracleContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", oracleContainer::getDriverClassName);
    }

    @Autowired
    DataRaceRepository repository;

    @Autowired
    DataRaceService service;

    @Test
    void findByIdString() {
        Data_Race test = repository.save(Data_Race.builder().name("test").build());
        UUID id = test.getId();
        Optional<Data_Race> byId = service.findById(id.toString());
        assertEquals(byId.get().getId(), id);
    }

    @Test
    void findByIdUUID() {
        Data_Race test = repository.save(Data_Race.builder().name("test").build());
        UUID id = test.getId();
        Optional<Data_Race> byId = service.findById(id);
        assertEquals(byId.get().getId(), id);
    }

    @Test
    void findByIdUUIDUnknown() {
        Optional<Data_Race> byId = service.findById(UUID.randomUUID());
        assertEquals(empty(), byId);
    }
}
