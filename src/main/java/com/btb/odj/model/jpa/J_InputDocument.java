package com.btb.odj.model.jpa;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class J_InputDocument {

    private UUID id;
    private UUID refId;
    private String name;
    private String country;
    private int laps;
    private Date raceDate;
    private List<J_PodiumPosition> podium;
}
