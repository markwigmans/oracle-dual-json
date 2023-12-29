package com.btb.odj.model.jpa;

import java.util.UUID;
import lombok.Builder;

@Builder
public record JSON_Driver(UUID refId, String name, String country, int points, JSON_Team team) {}
