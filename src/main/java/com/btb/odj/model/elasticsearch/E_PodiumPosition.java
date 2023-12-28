package com.btb.odj.model.elasticsearch;

import lombok.Data;

@Data
public class E_PodiumPosition {

    private E_Driver driver;
    private E_Race race;

    private int points;
    private int position;
}
