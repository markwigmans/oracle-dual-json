package com.btb.odj.model.elasticsearch;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class E_Team {

    private UUID refId;

    private String name;
    private String city;
    private String streetName;
    private String number;
    private String country;
    private int points;
}
