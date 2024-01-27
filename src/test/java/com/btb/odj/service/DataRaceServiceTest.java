package com.btb.odj.service;

import static com.btb.odj.config.CacheConfig.CACHE_RACES;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.btb.odj.model.Data_Race;
import com.btb.odj.repository.jpa.DataRaceRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

@SpringBootTest
@Disabled
class DataRaceServiceTest {

    @Autowired
    CacheManager cacheManager;

    @Autowired
    DataRaceRepository repository;

    @Autowired
    DataRaceService service;

    @Test
    void findByIdString() {
        Data_Race test = repository.save(Data_Race.builder().name("test").build());
        String id = test.getId().toString();
        Optional<Data_Race> byId = service.findById(id);
        assertEquals(byId, getCached(id));
    }

    @Test
    void findByIdUUID() {
        Data_Race test = repository.save(Data_Race.builder().name("test").build());
        UUID id = test.getId();
        Optional<Data_Race> byId = service.findById(id);
        assertEquals(byId, getCached(id.toString()));
    }

    @Test
    void findByIdUUIDUnknown() {
        repository.save(Data_Race.builder().name("test").build());
        assertEquals(empty(), getCached("unknown"));
    }

    private Optional<Data_Race> getCached(String id) {
        return ofNullable(cacheManager.getCache(CACHE_RACES)).map(c -> c.get(id, Data_Race.class));
    }
}
