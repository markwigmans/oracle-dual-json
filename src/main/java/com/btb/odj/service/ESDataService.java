package com.btb.odj.service;

import com.btb.odj.mapper.InputMapper;
import com.btb.odj.mapper.OutputMapper;
import com.btb.odj.model.jpa.J_Driver;
import com.btb.odj.model.jpa.J_Race;
import com.btb.odj.repository.elasticsearch.E_InputDocumentRepository;
import com.btb.odj.repository.elasticsearch.E_OutputDocumentRepository;
import com.btb.odj.service.messages.EntityMessage;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Component
@Slf4j
public class ESDataService extends AbstractDataService {

    private final J_DriverService jpaDriverService;
    private final J_RaceService jpaRaceService;
    private final E_InputDocumentRepository inputDocumentRepository;
    private final E_OutputDocumentRepository outputDocumentRepository;
    private final OutputMapper outputMapper;
    private final InputMapper inputMapper;

    public ESDataService(
            PlatformTransactionManager transactionManager,
            QueueService queueService,
            J_DriverService jpaDriverService,
            J_RaceService jpaRaceService,
            E_InputDocumentRepository inputDocumentRepository,
            E_OutputDocumentRepository outputDocumentRepository,
            OutputMapper outputMapper,
            InputMapper inputMapper) {
        super(transactionManager, queueService);
        this.jpaDriverService = jpaDriverService;
        this.jpaRaceService = jpaRaceService;
        this.inputDocumentRepository = inputDocumentRepository;
        this.outputDocumentRepository = outputDocumentRepository;
        this.outputMapper = outputMapper;
        this.inputMapper = inputMapper;
    }

    void processTeam(EntityMessage message) {
        log.debug("processTeam : {}", message);
    }

    void processDriver(EntityMessage message) {
        log.debug("processDriver : {}", message);
        Optional<J_Driver> driver = jpaDriverService.findById(message.id());
        driver.ifPresent(e -> outputDocumentRepository.save(outputMapper.from_J_to_E(e)));
    }

    void processRace(EntityMessage message) {
        log.debug("processRace : {}", message);
        Optional<J_Race> race = jpaRaceService.findById(message.id());
        race.ifPresent(r -> inputDocumentRepository.save(inputMapper.from_J_to_E(r)));
    }
}
