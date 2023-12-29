package com.btb.odj.service;

import com.btb.odj.model.jpa.J_Driver;
import com.btb.odj.model.jpa.J_Race;
import com.btb.odj.model.jpa.J_Team;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
abstract class AbstractDataService {

    private final PlatformTransactionManager transactionManager;

    private TransactionTemplate readOnlyTemplate;
    //private final ExecutorService executor = Executors.newWorkStealingPool();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    AbstractDataService(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @PostConstruct
    void init() {
        readOnlyTemplate = new TransactionTemplate(transactionManager);
        readOnlyTemplate.setReadOnly(true);
    }

    /**
     *
     * Method to be called in JMSListener method. It seems that 'concurrent' is handled diffrenctly with JMS topics, therefore this approach.
     */
    @JmsListener(destination = "#{queueConfiguration.getUpdateDataTopic()}", containerFactory = "topicConnectionFactory")
    void processMessage(Message<EntityMessage> message) {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() ->
                readOnlyTemplate.executeWithoutResult(status -> process(message.getPayload())), executor);
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

    abstract void processTeam(EntityMessage message);

    abstract void processDriver(EntityMessage message);

    abstract void processRace(EntityMessage message);
}
