package com.btb.odj.model.jpa;

import lombok.Builder;

import java.util.UUID;

@Builder
public record JSON_Team(
        UUID refId, String name, String city, String streetName, String number, String country, int points) {}
