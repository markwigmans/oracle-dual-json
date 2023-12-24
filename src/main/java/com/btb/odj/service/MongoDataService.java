package com.btb.odj.service;

import com.btb.odj.config.QueueConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MongoDataService {

    @JmsListener(
            destination = "#{queueConfiguration.getUpdateDataTopic()}",
            containerFactory = "topicConnectionFactory")
    public void receiveMessage(EntityMessage message) {
        log.info("received {} from the queue", message);
    }
}
