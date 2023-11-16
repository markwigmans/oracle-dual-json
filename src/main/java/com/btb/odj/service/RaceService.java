package com.btb.odj.service;

import com.btb.odj.config.DatasetConfig;
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
    public Race create() {
        Race race = repository.save(Race.builder()
                .name(faker.address().cityName())
                .country(faker.address().country())
                .laps(faker.number().numberBetween(config.getRaceMinLaps(), config.getRaceMaxLaps()))
                .raceDate(faker.date().past(config.getRacePreviousDays(), TimeUnit.DAYS))
                .build());
        race.setPodium(createPodium(race));
        return race;
    }

    List<PodiumPosition> createPodium(Race race) {
        return IntStream.range(0, pointList.size()).boxed()
                .map(i -> new PodiumPosition(race, driverService.giveRandomDriverPoints(pointList.get(i)), i+1))
                .toList();
    }
}
