package com.btb.odj.model.jpa;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class JSON_OutputDocument {

    private String id;
    private UUID refId;
    private JSON_Driver driver;
    private List<JSON_PodiumPosition> podium;
}
