package com.btb.odj.service;

import static com.btb.odj.config.CacheConfig.CACHE_DRIVERS;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.btb.odj.model.Data_Driver;
import com.btb.odj.repository.jpa.DataDriverRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

@SpringBootTest
@Disabled
class DataDriverServiceTest {

    @Autowired
    CacheManager cacheManager;

    @Autowired
    DataDriverRepository repository;

    @Autowired
    DataDriverService service;

    @Test
    void findByIdString() {
        Data_Driver test = repository.save(Data_Driver.builder().name("test").build());
        String id = test.getId().toString();
        Optional<Data_Driver> byId = service.findById(id);
        assertEquals(byId, getCached(id));
    }

    @Test
    void findByIdUUID() {
        Data_Driver test = repository.save(Data_Driver.builder().name("test").build());
        UUID id = test.getId();
        Optional<Data_Driver> byId = service.findById(id);
        assertEquals(byId, getCached(id.toString()));
    }

    @Test
    void findByIdUUIDUnknown() {
        repository.save(Data_Driver.builder().name("test").build());
        assertEquals(empty(), getCached("unknown"));
    }

    private Optional<Data_Driver> getCached(String id) {
        return ofNullable(cacheManager.getCache(CACHE_DRIVERS)).map(c -> c.get(id, Data_Driver.class));
    }
}
