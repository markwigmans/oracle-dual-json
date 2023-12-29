package com.btb.odj.service;

import com.btb.odj.config.QueueConfiguration;
import com.btb.odj.model.jpa.J_AbstractEntity;
import com.btb.odj.service.messages.EntityMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class QueueService {

    private final JmsTemplate jmsTemplate;
    private final QueueConfiguration queueConfiguration;

    public void sendUpdateMessage(List<J_AbstractEntity> entities) {
        entities.forEach(this::sendUpdateMessage);
    }

    public void sendUpdateMessage(J_AbstractEntity entity) {
        try {
            EntityMessage message = new EntityMessage(entity.getClass(), entity.getId().toString());
            log.debug("Sending ({}) to Topic: {}", message, queueConfiguration.getUpdateDataTopic());
            jmsTemplate.convertAndSend(queueConfiguration.getUpdateDataTopic(), message);
        } catch (Exception e) {
            log.error("Exception during send Message:", e);
        }
    }

    public void sendProcessedMessage(String correlationId, EntityMessage entityMessage) {
        try {
            log.debug("Sending ({}) to Topic: {}", entityMessage, queueConfiguration.getProcessedData());
            jmsTemplate.convertAndSend(queueConfiguration.getProcessedData(), entityMessage, m -> {
                m.setJMSCorrelationID(correlationId);
                return m;
            });
        } catch (Exception e) {
            log.error("Exception during send Message:", e);
        }
    }
}
