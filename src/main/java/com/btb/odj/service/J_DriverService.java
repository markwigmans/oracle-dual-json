package com.btb.odj.service;

import com.btb.odj.model.jpa.J_Driver;
import com.btb.odj.model.jpa.J_Team;
import com.btb.odj.repository.jpa.J_DriverRepository;
import com.github.javafaker.Faker;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class J_DriverService {

    private final Faker faker;
    private final J_DriverRepository repository;
    @PersistenceContext
    private final EntityManager entityManager;

    @Transactional
    public J_Driver create(J_Team team) {
        return repository.save(J_Driver.builder()
                .name(faker.name().name())
                .country(faker.address().country())
                .points(0)
                .team(team)
                .build());
    }

    public List<J_Driver> getWinners(float percentage, int limit) {
        // create the query itself, because the sample parameter doesn't work as a SQL parameter
        final float perc = percentage > 10 ? 10 : percentage;
        // enforce Locale to be sure that a '.' is used as separator (otherwise we get illegal SQL errors)
        final Query query = entityManager.createNativeQuery(String.format(Locale.US,
                "select * from driver sample(%f) ORDER BY DBMS_RANDOM.RANDOM FETCH FIRST :limit ROWS ONLY", perc),
                J_Driver.class);
        query.setParameter("limit", limit);
        return query.getResultList();
    }

    @Transactional
    public void updatePoints() {
        repository.updatePoints();
    }

    public Optional<J_Driver> findById(String id) {
        return findById(UUID.fromString(id));
    }

    public Optional<J_Driver> findById(UUID id) {
        return repository.findById(id);
    }

    public Page<J_Driver> findAll(Pageable page) {
        return repository.findAll(page);
    }
}
