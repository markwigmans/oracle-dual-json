package com.btb.odj.model.elasticsearch;

import lombok.Builder;

import java.util.UUID;

@Builder
public record E_Driver (
     UUID refId,
     String name,
     String country,
     int points,
     E_Team team) {
}
