package com.btb.odj.service;

import static com.btb.odj.util.Provider.*;

import com.btb.odj.service.provider.ProviderProperties;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RequestService {

    private final Optional<ESDataService> esDataService;
    private final Optional<JPADataService> jpaDataService;
    private final Optional<MongoDataService> mongoDataService;
    private final ProviderProperties properties;

    @SneakyThrows
    public Map<String, List<?>> findDriversWithMoreThan(int points) {
        final var futureM = properties.isActive(MongoDB)
                ? CompletableFuture.supplyAsync(() -> mongoDataService.get().findDriversWithMoreThan(points))
                : CompletableFuture.completedFuture(Collections.emptyList());
        final var futureE = properties.isActive(ElasticSearch)
                ? CompletableFuture.supplyAsync(() -> esDataService.get().findDriversWithMoreThan(points))
                : CompletableFuture.completedFuture(Collections.emptyList());
        final var futureJ = properties.isActive(JPA)
                ? CompletableFuture.supplyAsync(() -> jpaDataService.get().findDriversWithMoreThan(points))
                : CompletableFuture.completedFuture(Collections.emptyList());

        CompletableFuture.allOf(futureM, futureE, futureJ).join();
        return Map.of(MongoDB.label, futureM.join(), ElasticSearch.label, futureE.join(), JPA.label, futureJ.join());
    }
}
