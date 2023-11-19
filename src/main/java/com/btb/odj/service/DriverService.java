package com.btb.odj.service;

import com.btb.odj.model.jpa.Driver;
import com.btb.odj.model.jpa.Team;
import com.github.javafaker.Faker;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class DriverService {

    private final Faker faker;
    private final DriverRepository repository;
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Driver create(Team team) {
        return repository.save(Driver.builder()
                .name(faker.name().name())
                .country(faker.address().country())
                .points(0)
                .team(team)
                .build());
    }

    public List<Driver> getWinners(float percentage, int limit) {
        // create the query itself, because the sample parameter doesn't work as a SQL parameter
        final float perc = percentage > 50 ? 50 : percentage;
        final Query query = entityManager.createNativeQuery(String.format("select * from driver sample(%f) FETCH FIRST :limit ROWS ONLY", perc), Driver.class);
        query.setParameter("limit", limit);
        return query.getResultList();
    }

    @Transactional
    public void updatePoints() {
        repository.updatePoints();
    }
}
