package com.btb.odj.service;

import com.btb.odj.service.messages.EntityMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Component
@Slf4j
public class JPADataService extends AbstractDataService {

    public JPADataService(PlatformTransactionManager transactionManager, QueueService queueService) {
        super(transactionManager, queueService);
    }

    void processTeam(EntityMessage message) {
        log.debug("processTeam : {}", message);
    }

    void processDriver(EntityMessage message) {
        log.debug("processDriver : {}", message);
    }

    void processRace(EntityMessage message) {
        log.debug("processRace : {}", message);
    }
}
