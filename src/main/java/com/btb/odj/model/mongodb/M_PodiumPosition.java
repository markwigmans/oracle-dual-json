package com.btb.odj.model.mongodb;

import lombok.Builder;
import lombok.Data;

/**
 *
 */
@Data
@Builder
public class M_PodiumPosition {

    private M_Driver driver;
    private M_Team team;

    private int points;
    private int position;
}
