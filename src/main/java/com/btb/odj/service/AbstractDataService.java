package com.btb.odj.service;

import com.btb.odj.model.jpa.J_Driver;
import com.btb.odj.model.jpa.J_Race;
import com.btb.odj.model.jpa.J_Team;
import com.btb.odj.service.messages.EntityMessage;
import jakarta.annotation.PostConstruct;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.support.JmsHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Slf4j
abstract class AbstractDataService {

    private final PlatformTransactionManager transactionManager;
    private final QueueService queueService;

    private TransactionTemplate readOnlyTemplate;
    private final ExecutorService executor = Executors.newWorkStealingPool();

    AbstractDataService(PlatformTransactionManager transactionManager, QueueService queueService) {
        this.transactionManager = transactionManager;
        this.queueService = queueService;
    }

    @PostConstruct
    void init() {
        readOnlyTemplate = new TransactionTemplate(transactionManager);
        readOnlyTemplate.setReadOnly(true);
    }

    /**
     * Method to be called in JMSListener method. It seems that 'concurrent' is handled differently with JMS topics, therefore this approach.
     */
    @JmsListener(
            destination = "#{queueConfiguration.getUpdateDataTopic()}",
            containerFactory = "topicConnectionFactory")
    void processMessage(EntityMessage message, @Header(JmsHeaders.MESSAGE_ID) String messageId) {
        CompletableFuture<Void> future = CompletableFuture.runAsync(
                () -> readOnlyTemplate.executeWithoutResult(status -> process(message)), executor);
        future.whenComplete((r, ex) -> {
            if (ex != null) {
                log.error("Exception", ex);
            } else {
                queueService.sendProcessedMessage(messageId, message);
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
