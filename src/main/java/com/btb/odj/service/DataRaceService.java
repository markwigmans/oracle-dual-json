package com.btb.odj.service;

import com.btb.odj.config.DatasetConfig;
import com.btb.odj.model.Data_Driver;
import com.btb.odj.model.Data_PodiumPosition;
import com.btb.odj.model.Data_Race;
import com.btb.odj.repository.jpa.DataRaceRepository;
import com.github.javafaker.Faker;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class DataRaceService {

    private final Faker faker;
    private final DatasetConfig config;
    private final DataRaceRepository repository;
    private final DataDriverService jDriverService;

    private final List<Integer> pointList = List.of(25, 18, 15, 12, 10, 8, 6, 4, 2, 1);

    @Transactional
    public Data_Race create(int size) {
        Data_Race JRace = repository.save(Data_Race.builder()
                .name(faker.address().cityName())
                .country(faker.address().country())
                .laps(faker.number().numberBetween(config.getRaceMinLaps(), config.getRaceMaxLaps()))
                .raceDate(faker.date().past(config.getRacePreviousDays(), TimeUnit.DAYS))
                .build());
        JRace.setPodium(createPodium(JRace, size));
        return JRace;
    }

    List<Data_PodiumPosition> createPodium(Data_Race JRace, int size) {
        // it is in % and factor 10 just to be sure there are enough drivers found
        float percentage = pointList.size() * 1000f / size;
        final List<Data_Driver> winners = jDriverService.getWinners(percentage, pointList.size());

        return IntStream.range(0, pointList.size())
                .boxed()
                .filter(i -> i < winners.size())
                .map(i -> new Data_PodiumPosition(JRace, winners.get(i), pointList.get(i), i + 1))
                .toList();
    }

    @Cacheable("races")
    public Optional<Data_Race> findById(String id) {
        return findById(UUID.fromString(id));
    }

    public Optional<Data_Race> findById(UUID id) {
        return repository.findById(id);
    }

    public Page<Data_Race> findAll(Pageable page) {
        return repository.findAll(page);
    }
}
