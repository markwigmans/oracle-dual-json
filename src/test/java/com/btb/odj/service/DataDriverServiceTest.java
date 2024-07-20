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
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
class DataDriverServiceTest {

    @Autowired
    DataDriverRepository repository;

    @Autowired
    DataDriverService service;

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        TestContainers.registerProperties(registry);
    }

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
