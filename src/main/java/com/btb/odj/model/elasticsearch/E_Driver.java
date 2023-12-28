package com.btb.odj.model.elasticsearch;

import lombok.Data;

import java.util.UUID;

@Data
public class E_Driver {

    private UUID refId;

    private String name;
    private String country;
    private int points;

    private E_Team team;
}
