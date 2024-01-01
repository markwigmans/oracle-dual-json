package com.btb.odj.model.jpa;

import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class J_OutputDocument {

    private UUID id;
    private UUID refId;
    private J_Driver driver;
    private List<J_PodiumPosition> podium;
}
