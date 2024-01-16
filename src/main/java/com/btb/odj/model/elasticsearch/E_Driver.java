package com.btb.odj.model.elasticsearch;

import java.util.UUID;
import lombok.Builder;

@Builder
public record E_Driver(UUID refId, String name, String country, int points, E_Team team) {}
