package com.btb.odj.service;

import static com.btb.odj.config.CacheConfig.CACHE_DRIVERS;

import com.btb.odj.model.Data_Driver;
import com.btb.odj.model.Data_Team;
import com.btb.odj.repository.jpa.DataDriverRepository;
import com.github.javafaker.Faker;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
@CacheConfig(cacheNames = CACHE_DRIVERS)
public class DataDriverService {

    private final Faker faker;
    private final DataDriverRepository repository;

    @PersistenceContext
    private final EntityManager entityManager;

    @Transactional
    public Data_Driver create(Data_Team team) {
        return repository.save(Data_Driver.builder()
                .name(faker.name().name())
                .country(faker.address().country())
                .points(0)
                .team(team)
                .build());
    }

    public List<Data_Driver> getWinners(float percentage, int limit) {
        // create the query itself, because the sample parameter doesn't work as a SQL parameter
        final float perc = percentage > 10 ? 10 : percentage;
        // enforce Locale to be sure that a '.' is used as separator (otherwise we get illegal SQL errors)
        final Query query = entityManager.createNativeQuery(
                String.format(
                        Locale.US,
                        "select * from driver sample(%f) ORDER BY DBMS_RANDOM.RANDOM FETCH FIRST :limit ROWS ONLY",
                        perc),
                Data_Driver.class);
        query.setParameter("limit", limit);
        return query.getResultList();
    }

    @Transactional
    public void updatePoints() {
        repository.updatePoints();
    }

    @Cacheable(unless = "#result == null")
    public Optional<Data_Driver> findById(String id) {
        return findById(UUID.fromString(id));
    }

    @Cacheable(unless = "#result == null", key = "#id.toString()")
    public Optional<Data_Driver> findById(UUID id) {
        return repository.findById(id);
    }

    public Page<Data_Driver> findAll(Pageable page) {
        return repository.findAll(page);
    }
}
