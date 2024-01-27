package com.btb.odj.service;

import static com.btb.odj.util.Provider.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RequestService {

    private final ESDataService esDataService;
    private final JPADataService jpaDataService;
    private final MongoDataService mongoDataService;

    @SneakyThrows
    public Map<String, List<?>> findDriversWithMoreThan(int points) {
        CompletableFuture<? extends List<?>> futureM =
                CompletableFuture.supplyAsync(() -> mongoDataService.findDriversWithMoreThan(points));
        CompletableFuture<? extends List<?>> futureE =
                CompletableFuture.supplyAsync(() -> esDataService.findDriversWithMoreThan(points));
        CompletableFuture<? extends List<?>> futureJ =
                CompletableFuture.supplyAsync(() -> jpaDataService.findDriversWithMoreThan(points));

        CompletableFuture.allOf(futureM, futureE, futureJ).join();
        return Map.of(MongoDB.label, futureM.join(), ElasticSearch.label, futureE.join(), JPA.label, futureJ.join());
    }
}