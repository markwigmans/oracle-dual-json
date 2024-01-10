package com.btb.odj.service;

import com.btb.odj.model.Data_Team;
import com.btb.odj.repository.jpa.DataTeamRepository;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class DataTeamService {

    private final Faker faker;
    private final DataDriverService jDriverService;
    private final DataTeamRepository repository;

    @Transactional
    public Data_Team create(int maxDrivers) {
        Data_Team team = repository.save(Data_Team.builder()
                .name(faker.company().name())
                .city(faker.address().city())
                .streetName(faker.address().streetName())
                .number(faker.address().buildingNumber())
                .country(faker.address().country())
                .points(0)
                .build());
        final int count = faker.number().numberBetween(1, maxDrivers);
        team.setDrivers(IntStream.range(0, count)
                .mapToObj(t -> jDriverService.create(team))
                .toList());
        return team;
    }

    @Transactional
    public void updatePoints() {
        repository.updatePoints();
    }

    public Page<Data_Team> findAll(Pageable page) {
        return repository.findAll(page);
    }
}
