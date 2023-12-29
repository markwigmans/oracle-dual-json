package com.btb.odj.model.elasticsearch;

import lombok.Builder;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.UUID;

@Builder
public record E_Race(
        UUID refId,
        String name,
        String country,
        int laps,
        @Field(type = FieldType.Date, format = DateFormat.basic_date_time)
        Date raceDate) {
}
