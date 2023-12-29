package com.btb.odj.model.elasticsearch;

import lombok.Builder;

import java.util.UUID;

@Builder
public record E_Team(
        UUID refId,
        String name,
        String city,
        String streetName,
        String number,
        String country,
        int points) {
}
