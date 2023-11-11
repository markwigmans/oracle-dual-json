package com.btb.odj.service;

import com.btb.odj.config.DatasetConfig;
import com.btb.odj.model.jpa.Driver;
import com.btb.odj.model.jpa.Race;
import com.btb.odj.model.jpa.Team;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataService {

    private final Faker faker;

    private final DatasetConfig config;
    private final RaceRepository raceRepository;
    private final TeamRepository teamRepository;

    private final AtomicBoolean running = new AtomicBoolean(false);
    private final ExecutorService executor = Executors.newWorkStealingPool();

    public CompletableFuture<Void> createDataset(int size) {
        if (running.compareAndSet(false, true)) {
            return CompletableFuture.runAsync(() -> {
                log.info("Data creation started");
                saveRaces(size);
                saveTeams((int) (size * config.getTeamsMultiplier()), (int) (size * config.getDriversMultiplier()));
                running.getAndSet(false);
            });
        } else {
            log.info("Already running");
        }
        return CompletableFuture.completedFuture(null);
    }

    void saveRaces(int races) {
        final List<List<Integer>> groups = split(IntStream.range(0, races).boxed().toList(), 50);
        var array = groups.stream()
                .map(g -> CompletableFuture.supplyAsync(() -> raceRepository.saveAll(g.stream().map(t -> createRace()).toList()), executor))
                .toArray(CompletableFuture[]::new);
        try {
            CompletableFuture.allOf(array).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    Race createRace() {
        return Race.builder()
                .name(faker.address().cityName())
                .laps(faker.number().numberBetween(config.getRaceMinLaps(), config.getRaceMaxLaps()))
                .raceDate(faker.date().past(config.getRacePreviousDays(), TimeUnit.DAYS))
                .build();
    }

    void saveTeams(int teams, int maxDrivers) {
        final List<List<Integer>> groups = split(IntStream.range(0, teams).boxed().toList(), 50);
        var array = groups.stream()
                .map(g -> CompletableFuture.supplyAsync(() -> teamRepository.saveAll(g.stream().map(t -> createTeam(maxDrivers)).toList()), executor))
                .toArray(CompletableFuture[]::new);
        try {
            CompletableFuture.allOf(array).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    Team createTeam(int maxDrivers) {
        Team team = Team.builder()
                .name(faker.company().name())
                .city(faker.address().city())
                .streetName(faker.address().streetName())
                .number(faker.address().buildingNumber())
                .country(faker.address().country())
                .points(faker.number().numberBetween(0, config.getTeamMaxPoints()))
                .build();
        int drivers = faker.number().numberBetween((int) (maxDrivers * config.getMinDriversMultiplier()), maxDrivers);
        team.setDrivers(IntStream.range(0, drivers).mapToObj(t -> createDriver(team)).toList());
        return team;
    }

    Driver createDriver(Team team) {
        return Driver.builder()
                .name(faker.name().name())
                .country(faker.address().country())
                .points(faker.number().numberBetween(0, config.getDriverMaxPoints()))
                .team(team)
                .build();
    }

    List<List<Integer>> split(List<Integer> list, int size) {
        final Map<Integer, List<Integer>> groups = list.stream().collect(Collectors.groupingBy(s -> (s - 1) / size));
        return new ArrayList<>(groups.values());
    }
}
