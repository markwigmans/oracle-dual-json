package com.btb.odj.service;

import com.btb.odj.config.DatasetConfig;
import com.btb.odj.model.Data_AbstractEntity;
import com.btb.odj.service.messages.ProcessedMessage;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.support.JmsHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataService {

    private final DatasetConfig config;
    private final DataTeamService teamService;
    private final DataDriverService driverService;
    private final DataRaceService raceService;
    private final QueueService queueService;
    private final AtomicInteger counter = new AtomicInteger(0);
    private final AtomicBoolean syncStarted = new AtomicBoolean(false);

    @Value("${data.batch.size:100}")
    private int batchSize;

    private static final int PROCESSORS = 3;

    private final ExecutorService executor = Executors.newWorkStealingPool();
    private final AtomicBoolean running = new AtomicBoolean(false);

    public CompletableFuture<Void> createDataset(int size) {
        if (running.compareAndSet(false, true)) {
            var result = CompletableFuture.runAsync(() -> {
                log.info("Data creation started: {}", size);
                createTeams((int) (size * config.getTeamsMultiplier()));
                createRace(size);
                driverService.updatePoints();
                teamService.updatePoints();
                syncData();
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

    @SneakyThrows
    public void syncData() {
        if (!syncStarted.get()) {
            log.info("Start broadcast data");
            try {
                counter.set(0);
                syncStarted.compareAndSet(false, true);
                List<CompletableFuture<Void>> futures = new LinkedList<>();
                futures.add(broadcast(driverService::findAll));
                futures.add(broadcast(teamService::findAll));
                futures.add(broadcast(raceService::findAll));
                // wait till ready
                CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new))
                        .get();
            } finally {
                syncStarted.compareAndSet(true, false);
            }
            log.info("End broadcast data");
        } else {
            log.info("Broadcast already started");
        }
    }

    @JmsListener(
            destination = "#{queueConfiguration.getProcessedData()}",
            containerFactory = "queueConnectionFactory",
            concurrency = "${data.processed.concurrency:1-5}")
    void processMessage(ProcessedMessage message, @Header(JmsHeaders.CORRELATION_ID) String correlationId) {
        int value = counter.getAndDecrement();
        log.debug("{} : {} : processor: {} message: {}", value, correlationId, message.processor(), message.message());
        if (!syncStarted.get() && (value <= 1)) {
            log.info("All messages processed");
        }
    }

    private CompletableFuture<Void> broadcast(Function<PageRequest, Page<? extends Data_AbstractEntity>> func) {
        List<CompletableFuture<Void>> futures = new LinkedList<>();
        PageRequest pageRequest = PageRequest.ofSize(batchSize);
        Page<? extends Data_AbstractEntity> entiteitenPage;
        do {
            entiteitenPage = func.apply(pageRequest);
            final List<Data_AbstractEntity> copy = Collections.unmodifiableList(entiteitenPage.getContent());
            counter.addAndGet(PROCESSORS * copy.size());
            futures.add(CompletableFuture.runAsync(() -> queueService.sendUpdateMessage(copy), executor));
            pageRequest = pageRequest.next();
        } while (!entiteitenPage.isLast());
        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    @SneakyThrows
    void createTeams(int races) {
        final Collection<List<Integer>> groups =
                split(IntStream.range(0, races).boxed().toList(), batchSize);
        var array = groups.stream()
                .map(g -> CompletableFuture.supplyAsync(
                        () -> g.stream()
                                .map(t -> teamService.create(config.getMaxDrivers()))
                                .toList(),
                        executor))
                .toArray(CompletableFuture[]::new);
        // wait till ready
        CompletableFuture.allOf(array).get();
    }

    @SneakyThrows
    void createRace(int races) {
        final Collection<List<Integer>> groups =
                split(IntStream.range(0, races).boxed().toList(), batchSize);
        var array = groups.stream()
                .map(g -> CompletableFuture.supplyAsync(
                        () -> g.stream().map(t -> raceService.create(races)).toList(), executor))
                .toArray(CompletableFuture[]::new);
        // wait till ready
        CompletableFuture.allOf(array).get();
    }

    static Collection<List<Integer>> split(List<Integer> list, int size) {
        final Map<Integer, List<Integer>> groups = list.stream().collect(Collectors.groupingBy(s -> s / size));
        return groups.values();
    }
}
