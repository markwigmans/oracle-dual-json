package com.btb.odj.model.jpa;

import lombok.Builder;

@Builder
public record J_PodiumPosition(J_Driver driver, J_Race race, int points, int position) {}
