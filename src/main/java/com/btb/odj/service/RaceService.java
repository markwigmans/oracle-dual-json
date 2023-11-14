package com.btb.odj.service;

import com.btb.odj.config.DatasetConfig;
import com.btb.odj.model.jpa.Driver;
import com.btb.odj.model.jpa.Race;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class RaceService {

    private final Faker faker;
    private final DatasetConfig config;
    private final RaceRepository repository;
    private final DriverService driverService;

    private final List<Integer> pointList = List.of(25, 18, 15, 12, 10, 8, 6, 4, 2, 1);

    @Transactional
    public Race create() {
        Race race = repository.save(Race.builder()
                .name(faker.address().cityName())
                .country(faker.address().country())
                .laps(faker.number().numberBetween(config.getRaceMinLaps(), config.getRaceMaxLaps()))
                .raceDate(faker.date().past(config.getRacePreviousDays(), TimeUnit.DAYS))
                .build());
        race.setPodium(createPodium());
        return race;
    }

    List<Driver> createPodium() {
        return pointList.stream().map(driverService::giveRandomDriverPoints).toList();
    }
}
