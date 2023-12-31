package com.btb.odj.service;

import com.btb.odj.mapper.InputMapper;
import com.btb.odj.mapper.OutputMapper;
import com.btb.odj.model.Data_Driver;
import com.btb.odj.model.Data_Race;
import com.btb.odj.repository.mongodb.M_InputDocumentRepository;
import com.btb.odj.repository.mongodb.M_OutputDocumentRepository;
import com.btb.odj.service.messages.EntityMessage;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Component
@Slf4j
public class MongoDataService extends AbstractDataService {

    private final DataDriverService jpaDriverService;
    private final DataRaceService jpaRaceService;
    private final M_InputDocumentRepository inputDocumentRepository;
    private final M_OutputDocumentRepository outputDocumentRepository;
    private final OutputMapper outputMapper;
    private final InputMapper inputMapper;

    public MongoDataService(
            PlatformTransactionManager transactionManager,
            QueueService queueService,
            DataDriverService jpaDriverService,
            DataRaceService jpaRaceService,
            M_InputDocumentRepository inputDocumentRepository,
            M_OutputDocumentRepository outputDocumentRepository,
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
        Optional<Data_Driver> driver = jpaDriverService.findById(message.id());
        driver.ifPresent(e -> outputDocumentRepository.save(outputMapper.from_Data_to_M(e)));
    }

    void processRace(EntityMessage message) {
        log.debug("processRace : {}", message);
        Optional<Data_Race> race = jpaRaceService.findById(message.id());
        race.ifPresent(r -> inputDocumentRepository.save(inputMapper.from_Data_to_M(r)));
    }
}
