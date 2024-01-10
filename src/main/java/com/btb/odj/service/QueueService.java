package com.btb.odj.service;

import com.btb.odj.config.QueueConfiguration;
import com.btb.odj.model.Data_AbstractEntity;
import com.btb.odj.service.messages.EntityMessage;
import com.btb.odj.service.messages.ProcessedMessage;
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

    public void sendUpdateMessage(List<Data_AbstractEntity> entities) {
        entities.forEach(this::sendUpdateMessage);
    }

    public void sendUpdateMessage(Data_AbstractEntity entity) {
        try {
            EntityMessage message =
                    new EntityMessage(entity.getClass(), entity.getId().toString());
            log.debug("Sending ({}) to Topic: {}", message, queueConfiguration.getUpdateDataTopic());
            jmsTemplate.convertAndSend(queueConfiguration.getUpdateDataTopic(), message);
        } catch (Exception e) {
            log.error("Exception during send Message:", e);
        }
    }

    public void sendProcessedMessage(Class<?> processor, String correlationId, EntityMessage entityMessage) {
        try {
            log.debug("Sending ({}) to Topic: {}", entityMessage, queueConfiguration.getProcessedData());
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
