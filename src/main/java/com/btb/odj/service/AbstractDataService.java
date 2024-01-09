package com.btb.odj.service;

import com.btb.odj.model.Data_Driver;
import com.btb.odj.model.Data_Race;
import com.btb.odj.model.Data_Team;
import com.btb.odj.service.messages.EntityMessage;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.support.JmsHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Slf4j
abstract class AbstractDataService {

    private final QueueService queueService;
    private final TransactionTemplate readOnlyTemplate;
    private final ExecutorService executor;

    AbstractDataService(
            PlatformTransactionManager transactionManager, QueueService queueService, ExecutorService executor) {
        this.queueService = queueService;
        this.readOnlyTemplate = new TransactionTemplate(transactionManager);
        this.readOnlyTemplate.setReadOnly(true);
        this.executor = executor;
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
                queueService.sendProcessedMessage(getClass(), messageId, message);
            }
        });
    }

    private void process(final EntityMessage message) {
        if (message.type().equals(Data_Driver.class)) {
            processDriver(message);
        } else if (message.type().equals(Data_Team.class)) {
            processTeam(message);
        } else if (message.type().equals(Data_Race.class)) {
            processRace(message);
        } else {
            log.warn("Message({}) not processed", message);
        }
    }

    abstract void processTeam(EntityMessage message);

    abstract void processDriver(EntityMessage message);

    abstract void processRace(EntityMessage message);
}
