package com.btb.odj.service;

import com.btb.odj.config.DatasetConfig;
import com.btb.odj.model.jpa.AbstractEntity;
import com.btb.odj.model.jpa.Driver;
import com.btb.odj.model.jpa.Race;
import com.btb.odj.model.jpa.Team;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataService {

    private final DatasetConfig config;
    private final JTeamService teamService;
    private final JDriverService driverService;
    private final JRaceService raceService;
    private final QueueService queueService;

    @Value("${data.batch.size:100}")
    private int batchSize;

    private final ExecutorService executor = Executors.newWorkStealingPool();
    private final AtomicBoolean running = new AtomicBoolean(false);

    public CompletableFuture<Void> createDataset(int size) {
        if (running.compareAndSet(false, true)) {
            var result = CompletableFuture.runAsync(() -> {
                log.info("Data creation started");
                createTeams((int) (size * config.getTeamsMultiplier()));
                createRace(size);
                driverService.updatePoints();
                teamService.updatePoints();
                broadcastData();
            });
            result.whenComplete((r, ex) -> {
                running.getAndSet(false);
                if (ex != null) {
                    log.error("Exception", ex);
                }
            });
            return result;
        } else {
            log.info("Already running");
        }
        return CompletableFuture.completedFuture(null);
    }

    private void broadcastData() {
        broadcastDrivers();
        broadcastTeams();
        broadcastRaces();
    }

    private void broadcastDrivers() {
        PageRequest pageRequest = PageRequest.ofSize(batchSize);
        Page<Driver> entiteitenPage;
        do {
            entiteitenPage = driverService.findAll(pageRequest);
            queueService.sendUpdateMessage(Collections.unmodifiableList(entiteitenPage.getContent()));
            pageRequest = pageRequest.next();
        } while (!entiteitenPage.isLast());
    }

    private void broadcastTeams() {
        PageRequest pageRequest = PageRequest.ofSize(batchSize);
        Page<Team> entiteitenPage;
        do {
            entiteitenPage = teamService.findAll(pageRequest);
            queueService.sendUpdateMessage(Collections.unmodifiableList(entiteitenPage.getContent()));
            pageRequest = pageRequest.next();
        } while (!entiteitenPage.isLast());
    }

    private void broadcastRaces() {
        PageRequest pageRequest = PageRequest.ofSize(batchSize);
        Page<Race> entiteitenPage;
        do {
            entiteitenPage = raceService.findAll(pageRequest);
            queueService.sendUpdateMessage(Collections.unmodifiableList(entiteitenPage.getContent()));
            pageRequest = pageRequest.next();
        } while (!entiteitenPage.isLast());
    }

    @SneakyThrows
    void createTeams(int races) {
        final List<List<Integer>> groups = split(IntStream.range(0, races).boxed().toList(), batchSize);
        var array = groups.stream()
                .map(g -> CompletableFuture.supplyAsync(() -> g.stream()
                        .map(t -> teamService.create(config.getMaxDrivers()))
                        .toList(), executor))
                .toArray(CompletableFuture[]::new);
        // wait till ready
        CompletableFuture.allOf(array).get();
    }

    @SneakyThrows
    void createRace(int races) {
        final List<List<Integer>> groups = split(IntStream.range(0, races).boxed().toList(), batchSize);
        var array = groups.stream()
                .map(g -> CompletableFuture.supplyAsync(() -> g.stream()
                        .map(t -> raceService.create(races))
                        .toList(), executor))
                .toArray(CompletableFuture[]::new);
        // wait till ready
        CompletableFuture.allOf(array).get();
    }

    static List<List<Integer>> split(List<Integer> list, int size) {
        final Map<Integer, List<Integer>> groups = list.stream().collect(Collectors.groupingBy(s -> s / size));
        return new ArrayList<>(groups.values());
    }
}