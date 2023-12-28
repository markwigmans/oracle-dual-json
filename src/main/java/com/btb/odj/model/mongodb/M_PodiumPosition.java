package com.btb.odj.model.mongodb;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

/**
 *
 */
@Data
@Builder
public class M_PodiumPosition {

    private M_Driver driver;
    @DocumentReference(lazy = true)
    private M_Team team;

    private int points;
    private int position;
}
