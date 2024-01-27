package com.btb.odj.model.mongodb;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 *
 */
@Document("Input")
@Data
@Builder
public class M_InputDocument {

    @Id
    private String id;

    @Indexed
    private String refId;

    private String name;
    private String country;
    private int laps;
    private Date raceDate;

    private List<M_PodiumPosition> podium;
}
