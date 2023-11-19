package com.btb.odj.service;

import com.btb.odj.config.DatasetConfig;
import com.btb.odj.model.jpa.Driver;
import com.btb.odj.model.jpa.PodiumPosition;
import com.btb.odj.model.jpa.Race;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

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
    public Race create(int size) {
        Race race = repository.save(Race.builder()
                .name(faker.address().cityName())
                .country(faker.address().country())
                .laps(faker.number().numberBetween(config.getRaceMinLaps(), config.getRaceMaxLaps()))
                .raceDate(faker.date().past(config.getRacePreviousDays(), TimeUnit.DAYS))
                .build());
        race.setPodium(createPodium(race, size));
        return race;
    }

    List<PodiumPosition> createPodium(Race race, int size) {
        // it is in % and factor 10 just to be sure there are enough drivers found
        float percentage = size * 1000f / pointList.size();
        final List<Driver> drivers = driverService.getWinners(percentage, pointList.size());

        return IntStream.range(0, pointList.size()).boxed()
                .filter(i -> i < drivers.size())
                .map( i -> new PodiumPosition(race, drivers.get(i), pointList.get(i), i+1))
                .toList();

    }
}
