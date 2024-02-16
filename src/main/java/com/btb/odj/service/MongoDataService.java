package com.btb.odj.service;

import com.btb.odj.mapper.InputMapper;
import com.btb.odj.mapper.OutputMapper;
import com.btb.odj.model.mongodb.M_InputDocument;
import com.btb.odj.model.mongodb.M_OutputDocument;
import com.btb.odj.repository.mongodb.M_InputDocumentRepository;
import com.btb.odj.repository.mongodb.M_OutputDocumentRepository;
import com.btb.odj.service.messages.EntityMessages;
import com.btb.odj.service.provider.ProviderCondition;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.index.IndexResolver;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Conditional(ProviderCondition.class)
public class MongoDataService extends AbstractDataService {

    private final DataDriverService jpaDriverService;
    private final DataRaceService jpaRaceService;
    private final M_InputDocumentRepository inputDocumentRepository;
    private final M_OutputDocumentRepository outputDocumentRepository;
    private final OutputMapper outputMapper;
    private final InputMapper inputMapper;
    private final MongoTemplate mongoTemplate;

    public MongoDataService(
            QueueService queueService,
            DataDriverService jpaDriverService,
            DataRaceService jpaRaceService,
            M_InputDocumentRepository inputDocumentRepository,
            M_OutputDocumentRepository outputDocumentRepository,
            OutputMapper outputMapper,
            InputMapper inputMapper,
            MongoTemplate mongoTemplate) {
        super(queueService);
        this.jpaDriverService = jpaDriverService;
        this.jpaRaceService = jpaRaceService;
        this.inputDocumentRepository = inputDocumentRepository;
        this.outputDocumentRepository = outputDocumentRepository;
        this.outputMapper = outputMapper;
        this.inputMapper = inputMapper;
        this.mongoTemplate = mongoTemplate;
    }

    // Indexes are not created automatically, so enforce it
    @EventListener(ContextRefreshedEvent.class)
    public void initIndicesAfterStartup() {
        MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mappingContext =
                mongoTemplate.getConverter().getMappingContext();

        IndexResolver resolver = new MongoPersistentEntityIndexResolver(mappingContext);

        // consider only entities that are annotated with @Document
        mappingContext.getPersistentEntities().stream()
                .filter(it -> it.isAnnotationPresent(Document.class))
                .forEach(it -> {
                    IndexOperations indexOps = mongoTemplate.indexOps(it.getType());
                    resolver.resolveIndexFor(it.getType()).forEach(indexOps::ensureIndex);
                });
    }

    void processTeam(EntityMessages message) {
        log.debug("processTeam : {}", message.messages().size());
    }

    void processDriver(EntityMessages message) {
        List<M_OutputDocument> docs = message.messages().stream()
                .map(msg -> jpaDriverService.findById(msg.id()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(outputMapper::from_Data_to_M)
                .toList();
        outputDocumentRepository.saveAll(docs);
    }

    void processRace(EntityMessages message) {
        List<M_InputDocument> docs = message.messages().stream()
                .map(msg -> jpaRaceService.findById(msg.id()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(inputMapper::from_Data_to_M)
                .toList();
        inputDocumentRepository.saveAll(docs);
    }

    @Override
    List<?> findDriversWithMoreThan(int points) {
        Query query = new Query();
        query.addCriteria(Criteria.where("driver.points").gte(points));
        return mongoTemplate.find(query, M_OutputDocument.class);
    }
}
