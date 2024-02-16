package com.btb.odj.service;

import static org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRES_NEW;

import com.btb.odj.mapper.InputMapper;
import com.btb.odj.mapper.OutputMapper;
import com.btb.odj.model.Data_Driver;
import com.btb.odj.model.Data_Race;
import com.btb.odj.model.jpa.Data_InputDocument;
import com.btb.odj.model.jpa.Data_OutputDocument;
import com.btb.odj.model.jpa.J_InputDocument;
import com.btb.odj.model.jpa.J_OutputDocument;
import com.btb.odj.repository.jpa.Data_InputDocumentRepository;
import com.btb.odj.repository.jpa.Data_OutputDocumentRepository;
import com.btb.odj.service.messages.EntityMessages;
import com.btb.odj.service.provider.ProviderCondition;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Component
@Slf4j
@Conditional(ProviderCondition.class)
public class JPADataService extends AbstractDataService {

    private final DataDriverService jpaDriverService;
    private final DataRaceService jpaRaceService;
    private final Data_InputDocumentRepository inputDocumentRepository;
    private final Data_OutputDocumentRepository outputDocumentRepository;
    private final OutputMapper outputMapper;
    private final InputMapper inputMapper;
    private final ObjectMapper objectMapper;
    private final EntityManager entityManager;

    public JPADataService(
            QueueService queueService,
            DataDriverService jpaDriverService,
            DataRaceService jpaRaceService,
            Data_InputDocumentRepository inputDocumentRepository,
            Data_OutputDocumentRepository outputDocumentRepository,
            OutputMapper outputMapper,
            InputMapper inputMapper,
            EntityManager entityManager) {
        super(queueService);
        this.jpaDriverService = jpaDriverService;
        this.jpaRaceService = jpaRaceService;
        this.inputDocumentRepository = inputDocumentRepository;
        this.outputDocumentRepository = outputDocumentRepository;
        this.outputMapper = outputMapper;
        this.inputMapper = inputMapper;
        this.entityManager = entityManager;

        this.objectMapper = new ObjectMapper()
                .enable(SerializationFeature.INDENT_OUTPUT)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    void processTeam(EntityMessages message) {
        log.debug("processTeam : {}", message.messages().size());
    }

    void processDriver(EntityMessages message) {
        List<Data_OutputDocument> docs = message.messages().stream()
                .map(msg -> jpaDriverService.findById(msg.id()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(this::transform)
                .toList();
        outputDocumentRepository.saveAll(docs);
    }

    @SneakyThrows
    Data_OutputDocument transform(Data_Driver driver) {
        J_OutputDocument document = outputMapper.from_Data_to_J(driver);
        Data_OutputDocument.Data_OutputDocumentBuilder<?, ?> builder = Data_OutputDocument.builder();
        builder.json(objectMapper.writeValueAsString(document));
        builder.id(document.id());
        return builder.build();
    }

    void processRace(EntityMessages message) {
        List<Data_InputDocument> docs = message.messages().stream()
                .map(msg -> jpaRaceService.findById(msg.id()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(this::transform)
                .toList();
        inputDocumentRepository.saveAll(docs);
    }

    @SneakyThrows
    Data_InputDocument transform(Data_Race race) {
        J_InputDocument document = inputMapper.from_Data_to_J(race);
        Data_InputDocument.Data_InputDocumentBuilder<?, ?> builder = Data_InputDocument.builder();
        builder.json(objectMapper.writeValueAsString(document));
        builder.id(document.id());
        return builder.build();
    }

    @Override
    List<?> findDriversWithMoreThan(int points) {
        String query =
                String.format("SELECT json_query(json, '$[*]?(@.driver.points >= %d)') FROM OUTPUT_DOCUMENT", points);

        Query q = entityManager.createNativeQuery(query);
        List<String> resultList = q.getResultList();
        return resultList.stream().filter(Objects::nonNull).map(this::transfer).toList();
    }

    @SneakyThrows
    J_OutputDocument transfer(String json) {
        return objectMapper.readValue(json, J_OutputDocument.class);
    }
}
