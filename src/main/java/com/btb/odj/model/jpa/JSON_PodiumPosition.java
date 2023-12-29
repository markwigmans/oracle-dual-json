package com.btb.odj.model.jpa;

import lombok.Builder;

@Builder
public record JSON_PodiumPosition(JSON_Driver driver, JSON_Race race, int points, int position) {}
