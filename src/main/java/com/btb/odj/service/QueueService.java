package com.btb.odj.service;

import com.btb.odj.config.QueueConfiguration;
import com.btb.odj.model.Data_AbstractEntity;
import com.btb.odj.service.messages.EntityMessage;
import com.btb.odj.service.messages.EntityMessages;
import com.btb.odj.service.messages.ProcessedMessage;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class QueueService {

    private final JmsTemplate jmsTemplate;
    private final QueueConfiguration queueConfiguration;

    public void sendUpdateMessage(List<Data_AbstractEntity> entities) {
        if (!entities.isEmpty()) {
            Data_AbstractEntity first = entities.get(0);
            EntityMessages message = new EntityMessages(
                    first.getClass(),
                    entities.stream()
                            .map(e -> new EntityMessage(e.getClass(), e.getId().toString()))
                            .toList());
            sendUpdateMessage(message);
        }
    }

    void sendUpdateMessage(EntityMessages message) {
        try {
            log.debug(
                    "Sending ({},{}) to Topic: {}",
                    message.type(),
                    message.messages().size(),
                    queueConfiguration.getUpdateDataTopic());
            jmsTemplate.convertAndSend(queueConfiguration.getUpdateDataTopic(), message);
        } catch (Exception e) {
            log.error("Exception during send Message:", e);
        }
    }

    public void sendProcessedMessage(Class<?> processor, String correlationId, EntityMessages entityMessage) {
        try {
            log.debug(
                    "Sending ({},{}) to Topic: {}",
                    entityMessage.type(),
                    entityMessage.messages().size(),
                    queueConfiguration.getProcessedData());
            ProcessedMessage message = new ProcessedMessage(processor, entityMessage);

            jmsTemplate.convertAndSend(queueConfiguration.getProcessedData(), message, m -> {
                m.setJMSCorrelationID(correlationId);
                return m;
            });
        } catch (Exception e) {
            log.error("Exception during send Message:", e);
        }
    }
}
