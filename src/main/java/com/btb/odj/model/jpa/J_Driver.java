package com.btb.odj.model.jpa;

import lombok.Builder;

import java.util.UUID;

@Builder
public record J_Driver(UUID refId, String name, String country, int points, J_Team team) {}
