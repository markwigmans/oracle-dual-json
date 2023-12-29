package com.btb.odj.model.jpa;

import lombok.Builder;

import java.util.UUID;

@Builder
public record JSON_Driver(UUID refId, String name, String country, int points, JSON_Team team) {}
