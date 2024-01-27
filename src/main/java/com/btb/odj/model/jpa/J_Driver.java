package com.btb.odj.model.jpa;

import java.util.UUID;
import lombok.Builder;

@Builder
public record J_Driver(UUID refId, String name, String country, int points, J_Team team) {}
