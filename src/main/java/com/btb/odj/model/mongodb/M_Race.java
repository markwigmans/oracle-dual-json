package com.btb.odj.model.mongodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@Document("Race")
@AllArgsConstructor
public class M_Race {

    @Id
    private String id;
    private UUID refId;

    private String name;
    private String country;
    private int laps;
    private Date raceDate;

    private List<M_PodiumPosition> podium;
}
