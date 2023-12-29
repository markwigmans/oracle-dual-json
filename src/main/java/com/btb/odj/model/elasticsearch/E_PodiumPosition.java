package com.btb.odj.model.elasticsearch;

import lombok.Builder;

@Builder
public record E_PodiumPosition(
     E_Driver driver,
     E_Race race,
     int points,
     int position) {
}
