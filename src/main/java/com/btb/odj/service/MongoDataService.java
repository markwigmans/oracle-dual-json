package com.btb.odj.service;

import com.btb.odj.mapper.DriverMapper;
import com.btb.odj.mapper.RaceMapper;
import com.btb.odj.mapper.TeamMapper;
import com.btb.odj.model.jpa.J_Driver;
import com.btb.odj.model.jpa.J_Race;
import com.btb.odj.model.jpa.J_Team;
import com.btb.odj.repository.mongodb.M_DriverRepository;
import com.btb.odj.repository.mongodb.M_RaceRepository;
import com.btb.odj.repository.mongodb.M_TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class MongoDataService {

    private final J_DriverService jpaDriverService;
    private final J_RaceService jpaRaceService;
    private final J_TeamService jpaTeamService;
    private final M_DriverRepository mongodbDriverRepo;
    private final M_RaceRepository mongodbRaceRepo;
    private final M_TeamRepository mongodbTeamRepo;

    private final DriverMapper driverMapper;
    private final RaceMapper raceMapper;
    private final TeamMapper teamMapper;

    //@JmsListener(destination = "#{queueConfiguration.getUpdateDataTopic()}", containerFactory = "topicConnectionFactory")
    @Transactional(readOnly = true)
    public void receiveMessage(EntityMessage message) {
        if (message.type().equals(J_Driver.class)) {
            processDriver(message);
        } else if (message.type().equals(J_Team.class)) {
            processTeam(message);
        } else if (message.type().equals(J_Race.class)) {
            processRace(message);
        } else {
            log.warn("Message({}) not processed", message);
        }
    }

    private void processTeam(EntityMessage message) {
        log.info("processTeam : {}", message);
        Optional<J_Team> team = jpaTeamService.findById(message.id());
        team.ifPresent(e -> mongodbTeamRepo.save(teamMapper.transform_J_to_M(e)));
    }

    private void processDriver(EntityMessage message) {
        log.info("processDriver : {}", message);
        Optional<J_Driver> driver = jpaDriverService.findById(message.id());
        driver.ifPresent(e -> mongodbDriverRepo.save(driverMapper.transform_J_to_M(e)));
    }

    private void processRace(EntityMessage message) {
        log.info("processRace : {}", message);
        Optional<J_Race> race = jpaRaceService.findById(message.id());
        race.ifPresent(e -> mongodbRaceRepo.save(raceMapper.transform_J_to_M(e)));
    }
}

