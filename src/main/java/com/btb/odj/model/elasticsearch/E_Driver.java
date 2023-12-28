package com.btb.odj.model.elasticsearch;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class E_Driver {

    private UUID refId;

    private String name;
    private String country;
    private int points;

    private E_Team team;
}
