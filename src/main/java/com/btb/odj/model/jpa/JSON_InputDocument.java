package com.btb.odj.model.jpa;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class JSON_InputDocument {

    private UUID refId;
    private String name;
    private String country;
    private int laps;
    private Date raceDate;
    private List<JSON_PodiumPosition> podium;
}
