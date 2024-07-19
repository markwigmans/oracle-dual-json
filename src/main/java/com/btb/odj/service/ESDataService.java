package com.btb.odj.service;

import com.btb.odj.mapper.InputMapper;
import com.btb.odj.mapper.OutputMapper;
import com.btb.odj.model.Data_Driver;
import com.btb.odj.model.Data_Race;
import com.btb.odj.model.elasticsearch.E_OutputDocument;
import com.btb.odj.repository.elasticsearch.E_InputDocumentRepository;
import com.btb.odj.repository.elasticsearch.E_OutputDocumentRepository;
import com.btb.odj.service.messages.EntityMessage;
import com.btb.odj.service.provider.ProviderCondition;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Conditional;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchOperations;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Component
@Slf4j
@Conditional(ProviderCondition.class)
public class ESDataService extends AbstractDataService {

    private final DataDriverService jpaDriverService;
    private final DataRaceService jpaRaceService;
    private final E_InputDocumentRepository inputDocumentRepository;
    private final E_OutputDocumentRepository outputDocumentRepository;
    private final OutputMapper outputMapper;
    private final InputMapper inputMapper;
    private final SearchOperations searchOperations;

    public ESDataService(
            PlatformTransactionManager transactionManager,
            QueueService queueService,
            DataDriverService jpaDriverService,
            DataRaceService jpaRaceService,
            E_InputDocumentRepository inputDocumentRepository,
            E_OutputDocumentRepository outputDocumentRepository,
            OutputMapper outputMapper,
            InputMapper inputMapper,
            SearchOperations searchOperations) {
        super(transactionManager, queueService);
        this.jpaDriverService = jpaDriverService;
        this.jpaRaceService = jpaRaceService;
        this.inputDocumentRepository = inputDocumentRepository;
        this.outputDocumentRepository = outputDocumentRepository;
        this.outputMapper = outputMapper;
        this.inputMapper = inputMapper;
        this.searchOperations = searchOperations;
    }

    void processTeam(EntityMessage message) {
        log.debug("processTeam : {}", message);
    }

    void processDriver(EntityMessage message) {
        log.debug("processDriver : {}", message);
        Optional<Data_Driver> driver = jpaDriverService.findById(message.id());
        driver.ifPresent(e -> outputDocumentRepository.save(outputMapper.from_Data_to_E(e)));
    }

    void processRace(EntityMessage message) {
        log.debug("processRace : {}", message);
        Optional<Data_Race> race = jpaRaceService.findById(message.id());
        race.ifPresent(r -> inputDocumentRepository.save(inputMapper.from_Data_to_E(r)));
    }

    @Override
    List<?> findDriversWithMoreThan(int points) {
        Criteria criteria = Criteria.where("driver.points").greaterThanEqual(points);
        Query query = new CriteriaQuery(criteria);
        SearchHits<E_OutputDocument> output = searchOperations.search(query, E_OutputDocument.class);
        return output.getSearchHits();
    }
}
