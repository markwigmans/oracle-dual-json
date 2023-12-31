package com.btb.odj.util;

import static org.hibernate.generator.EventTypeSets.INSERT_ONLY;

import com.github.f4b6a3.uuid.UuidCreator;
import java.util.EnumSet;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.BeforeExecutionGenerator;
import org.hibernate.generator.EventType;

public class UuidV7Generator implements BeforeExecutionGenerator {

    @Override
    public Object generate(
            SharedSessionContractImplementor session, Object owner, Object currentValue, EventType eventType) {
        // actually generate a UUID and transform it to the required type
        return UuidCreator.getTimeOrderedEpoch();
    }

    @Override
    public EnumSet<EventType> getEventTypes() {
        // UUIDs are only assigned on insert, and never regenerated
        return INSERT_ONLY;
    }
}
