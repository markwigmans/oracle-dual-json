package com.btb.odj.model.mongodb;

import lombok.Builder;

@Builder
public record M_Driver(String refId, String name, String country, int points, M_Team team) {}
