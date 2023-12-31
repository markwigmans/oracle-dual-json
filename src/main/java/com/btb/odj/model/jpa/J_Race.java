package com.btb.odj.model.jpa;

import java.util.Date;
import java.util.UUID;
import lombok.Builder;

@Builder
public record J_Race(UUID refId, String name, String country, int laps, Date raceDate) {}
