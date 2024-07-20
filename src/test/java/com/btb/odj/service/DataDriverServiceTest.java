package com.btb.odj.service;

import static java.util.Optional.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.btb.odj.model.Data_Driver;
import com.btb.odj.repository.jpa.DataDriverRepository;
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
class DataDriverServiceTest {

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
    DataDriverRepository repository;

    @Autowired
    DataDriverService service;

    @Test
    void findByIdString() {
        Data_Driver test = repository.save(Data_Driver.builder().name("test").build());
        UUID id = test.getId();
        Optional<Data_Driver> byId = service.findById(id.toString());
        assertEquals(byId.get().getId(), id);
    }

    @Test
    void findByIdUUID() {
        Data_Driver test = repository.save(Data_Driver.builder().name("test").build());
        UUID id = test.getId();
        Optional<Data_Driver> byId = service.findById(id);
        assertEquals(byId.get().getId(), id);
    }

    @Test
    void findByIdUUIDUnknown() {
        Data_Driver test = repository.save(Data_Driver.builder().name("test").build());
        UUID id = test.getId();
        Optional<Data_Driver> byId = service.findById(UUID.randomUUID());
        assertEquals(empty(), byId);
    }
}
