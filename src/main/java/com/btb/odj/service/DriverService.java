package com.btb.odj.service;

import com.btb.odj.model.jpa.Driver;
import com.btb.odj.model.jpa.Team;
import com.github.javafaker.Faker;
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

    @Transactional
    public Driver create(Team team) {
        return repository.save(Driver.builder()
                .name(faker.name().name())
                .country(faker.address().country())
                .points(0)
                .team(team)
                .build());
    }

    public List<Driver> getWinners(int limit) {
        return repository.findRandomDrivers(limit);
    }

    @Transactional
    public Driver givePoints(Driver driver, int points) {
        driver.setPoints(driver.getPoints() + points);
        // update team points
        int teamPoints = driver.getTeam().getPoints();
        driver.getTeam().setPoints(teamPoints + points);
        return driver;
    }
}
