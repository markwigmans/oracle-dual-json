package com.btb.odj.model.jpa;

import lombok.Builder;

import java.util.Date;
import java.util.UUID;

@Builder
public record J_Race(UUID refId, String name, String country, int laps, Date raceDate) {}
