package com.btb.odj.service;

import com.btb.odj.mapper.InputMapper;
import com.btb.odj.mapper.OutputMapper;
import com.btb.odj.model.elasticsearch.E_InputDocument;
import com.btb.odj.model.elasticsearch.E_OutputDocument;
import com.btb.odj.repository.elasticsearch.E_InputDocumentRepository;
import com.btb.odj.repository.elasticsearch.E_OutputDocumentRepository;
import com.btb.odj.service.messages.EntityMessages;
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
            QueueService queueService,
            DataDriverService jpaDriverService,
            DataRaceService jpaRaceService,
            E_InputDocumentRepository inputDocumentRepository,
            E_OutputDocumentRepository outputDocumentRepository,
            OutputMapper outputMapper,
            InputMapper inputMapper,
            SearchOperations searchOperations) {
        super(queueService);
        this.jpaDriverService = jpaDriverService;
        this.jpaRaceService = jpaRaceService;
        this.inputDocumentRepository = inputDocumentRepository;
        this.outputDocumentRepository = outputDocumentRepository;
        this.outputMapper = outputMapper;
        this.inputMapper = inputMapper;
        this.searchOperations = searchOperations;
    }

    void processTeam(EntityMessages message) {
        log.debug("processTeam : {}", message.messages().size());
    }

    void processDriver(EntityMessages message) {
        List<E_OutputDocument> docs = message.messages().stream()
                .map(msg -> jpaDriverService.findById(msg.id()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(outputMapper::from_Data_to_E)
                .toList();
        outputDocumentRepository.saveAll(docs);
    }

    void processRace(EntityMessages message) {
        List<E_InputDocument> docs = message.messages().stream()
                .map(msg -> jpaRaceService.findById(msg.id()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(inputMapper::from_Data_to_E)
                .toList();
        inputDocumentRepository.saveAll(docs);
    }

    @Override
    List<?> findDriversWithMoreThan(int points) {
        Criteria criteria = Criteria.where("driver.points").greaterThanEqual(points);
        Query query = new CriteriaQuery(criteria);
        SearchHits<E_OutputDocument> output = searchOperations.search(query, E_OutputDocument.class);
        return output.getSearchHits();
    }
}
