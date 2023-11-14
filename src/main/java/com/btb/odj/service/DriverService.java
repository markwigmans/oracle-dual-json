package com.btb.odj.service;

import com.btb.odj.model.jpa.Driver;
import com.btb.odj.model.jpa.Team;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class DriverService {

    private final Faker faker;
    private final DriverRepository repository;
    private final List<UUID> drivers = new ArrayList<>();
    // use seed to make it reproduceable
    private final Random rand = new Random(10);

    @Transactional
    public Driver create(Team team) {
        final Driver driver = repository.save(Driver.builder()
                .name(faker.name().name())
                .country(faker.address().country())
                .points(0)
                .team(team)
                .build());
        drivers.add(driver.getId());
        return driver;
    }

    Driver giveRandomDriverPoints(int points) {
        final UUID id = drivers.get(rand.nextInt(drivers.size()));
        log.debug("lock Id: {}", id);
        Driver driver = repository.getReferenceById(id);
        driver.setPoints(driver.getPoints() + points);
        return driver;
    }
}
