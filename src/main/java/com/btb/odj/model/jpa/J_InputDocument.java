package com.btb.odj.model.jpa;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record J_InputDocument(
        UUID id, UUID refId, String name, String country, int laps, Date raceDate, List<J_PodiumPosition> podium) {}
