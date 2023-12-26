package com.btb.odj.service;

import com.btb.odj.model.jpa.J_Team;
import com.btb.odj.repository.jpa.J_TeamRepository;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class J_TeamService {

    private final Faker faker;
    private final J_DriverService jDriverService;
    private final J_TeamRepository repository;

    @Transactional
    public J_Team create(int maxDrivers) {
        J_Team team = repository.save(J_Team.builder()
                .name(faker.company().name())
                .city(faker.address().city())
                .streetName(faker.address().streetName())
                .number(faker.address().buildingNumber())
                .country(faker.address().country())
                .points(0)
                .build());
        final int count = faker.number().numberBetween(1, maxDrivers);
        team.setDrivers(IntStream.range(0, count).mapToObj(t -> jDriverService.create(team)).toList());
        return team;
    }

    @Transactional
    public void updatePoints() {
        repository.updatePoints();
    }

    public Optional<J_Team> findById(String id) {
        return findById(UUID.fromString(id));
    }

    public Optional<J_Team> findById(UUID id) {
        return repository.findById(id);
    }

    public Page<J_Team> findAll(Pageable page) {
        return repository.findAll(page);
    }
}
