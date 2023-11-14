package com.btb.odj.service;

import com.btb.odj.model.jpa.Team;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class TeamService {

    private final Faker faker;
    private final DriverService driverService;
    private final TeamRepository repository;

    @Transactional
    public Team create(int maxDrivers) {
        Team team = repository.save(Team.builder()
                .name(faker.company().name())
                .city(faker.address().city())
                .streetName(faker.address().streetName())
                .number(faker.address().buildingNumber())
                .country(faker.address().country())
                .points(0)
                .build());
        final int count = faker.number().numberBetween(1, maxDrivers);
        team.setDrivers(IntStream.range(0, count).mapToObj(t -> driverService.create(team)).toList());
        return team;
    }
}
