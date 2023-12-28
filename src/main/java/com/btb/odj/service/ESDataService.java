package com.btb.odj.service;

import com.btb.odj.mapper.InputMapper;
import com.btb.odj.mapper.OutputMapper;
import com.btb.odj.model.elasticsearch.E_InputDocument;
import com.btb.odj.model.elasticsearch.E_OutputDocument;
import com.btb.odj.model.jpa.J_Driver;
import com.btb.odj.model.jpa.J_Race;
import com.btb.odj.model.jpa.J_Team;
import com.btb.odj.repository.elasticsearch.E_InputDocumentRepository;
import com.btb.odj.repository.elasticsearch.E_OutputDocumentRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ESDataService {

    private final J_DriverService jpaDriverService;
    private final J_RaceService jpaRaceService;
    private final E_InputDocumentRepository inputDocumentRepository;
    private final E_OutputDocumentRepository outputDocumentRepository;
    private final OutputMapper outputMapper;
    private final InputMapper inputMapper;
    private final PlatformTransactionManager transactionManager;

    private TransactionTemplate readOnlyTemplate;
    private final ExecutorService executor = Executors.newWorkStealingPool();

    @PostConstruct
    void init() {
        readOnlyTemplate = new TransactionTemplate(transactionManager);
        readOnlyTemplate.setReadOnly(true);
    }

    @JmsListener(
            destination = "#{queueConfiguration.getUpdateDataTopic()}",
            containerFactory = "topicConnectionFactory")
    public void receiveMessage(final EntityMessage message) {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> readOnlyTemplate.executeWithoutResult(status -> process(message)), executor);
        future.whenComplete((r,ex) -> {
           if (ex != null) {
               log.error("Exception", ex);
           }
        });
    }

    private void process(final EntityMessage message) {
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
        log.debug("processTeam : {}", message);
    }

    private void processDriver(EntityMessage message) {
        log.debug("processDriver : {}", message);
        Optional<J_Driver> driver = jpaDriverService.findById(message.id());
        driver.ifPresent(e -> {
           E_OutputDocument result =  outputMapper.from_J_to_E(e);
           outputDocumentRepository.save(result);
        });
    }

    private void processRace(EntityMessage message) {
        log.debug("processRace : {}", message);
        Optional<J_Race> race = jpaRaceService.findById(message.id());
        race.ifPresent(r -> {
            E_InputDocument result = inputMapper.from_J_to_E(r);
            inputDocumentRepository.save(result);
        });
    }
}
