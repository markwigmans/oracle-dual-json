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
import com.btb.odj.service.messages.EntityMessage;
import com.btb.odj.service.provider.ProviderCondition;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.micrometer.core.annotation.Timed;
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
    private final TransactionTemplate readWriteTemplate;
    private final EntityManager entityManager;

    public JPADataService(
            PlatformTransactionManager transactionManager,
            QueueService queueService,
            DataDriverService jpaDriverService,
            DataRaceService jpaRaceService,
            Data_InputDocumentRepository inputDocumentRepository,
            Data_OutputDocumentRepository outputDocumentRepository,
            OutputMapper outputMapper,
            InputMapper inputMapper,
            EntityManager entityManager) {
        super(transactionManager, queueService);
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

        this.readWriteTemplate = new TransactionTemplate(transactionManager);
        this.readWriteTemplate.setPropagationBehavior(PROPAGATION_REQUIRES_NEW);
    }

    void processTeam(EntityMessage message) {
        log.debug("processTeam : {}", message);
    }

    void processDriver(EntityMessage message) {
        log.debug("processDriver : {}", message);
        Optional<Data_Driver> driver = jpaDriverService.findById(message.id());
        driver.ifPresent(e -> {
            Data_OutputDocument document = transform(e);
            readWriteTemplate.execute(status -> outputDocumentRepository.save(document));
        });
    }

    @SneakyThrows
    Data_OutputDocument transform(Data_Driver driver) {
        J_OutputDocument document = outputMapper.from_Data_to_J(driver);
        Data_OutputDocument.Data_OutputDocumentBuilder<?, ?> builder = Data_OutputDocument.builder();
        builder.json(objectMapper.writeValueAsString(document));
        builder.id(document.id());
        return builder.build();
    }

    void processRace(EntityMessage message) {
        log.debug("processRace : {}", message);
        Optional<Data_Race> race = jpaRaceService.findById(message.id());
        race.ifPresent(r -> {
            Data_InputDocument document = transform(r);
            readWriteTemplate.execute(status -> inputDocumentRepository.save(document));
        });
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
