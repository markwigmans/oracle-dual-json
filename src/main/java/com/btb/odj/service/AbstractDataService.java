package com.btb.odj.service;

import com.btb.odj.model.Data_Driver;
import com.btb.odj.model.Data_Race;
import com.btb.odj.model.Data_Team;
import com.btb.odj.service.messages.EntityMessage;
import java.util.List;
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

    AbstractDataService(PlatformTransactionManager transactionManager, QueueService queueService) {
        this.queueService = queueService;
        this.readOnlyTemplate = new TransactionTemplate(transactionManager);
        this.readOnlyTemplate.setReadOnly(true);
    }

    /**
     * Method to be called in JMSListener method.
     */
    @JmsListener(
            destination = "#{queueConfiguration.getUpdateDataTopic()}",
            containerFactory = "topicConnectionFactory",
            concurrency = "1")
    void processMessage(EntityMessage message, @Header(JmsHeaders.MESSAGE_ID) String messageId) {
        try {
            readOnlyTemplate.executeWithoutResult(status -> process(message));
        } catch (Exception ex) {
            log.error("Exception", ex);
        } finally {
            queueService.sendProcessedMessage(getClass(), messageId, message);
        }
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

    abstract List<?> findDriversWithMoreThan(int points);
}
