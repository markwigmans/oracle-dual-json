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
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest
@Import(TestContainersConfig.class)
class DataRaceServiceTest {

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
