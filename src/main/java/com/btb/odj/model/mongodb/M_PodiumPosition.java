package com.btb.odj.model.mongodb;

import lombok.Builder;

@Builder
public record M_PodiumPosition(
        M_Driver driver,
        M_Race race,
        int points,
        int position) {
}
