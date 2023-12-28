package com.btb.odj.model.elasticsearch;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.UUID;

/**
 *
 */
@Data
@Builder
public class E_Race {

    private UUID refId;

    private String name;
    private String country;
    private int laps;
    @Field(type= FieldType.Date, format= DateFormat.basic_date_time)
    private Date raceDate;
}
