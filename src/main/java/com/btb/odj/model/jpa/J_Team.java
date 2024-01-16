package com.btb.odj.model.jpa;

import java.util.UUID;
import lombok.Builder;

@Builder
public record J_Team(
        UUID refId, String name, String city, String streetName, String number, String country, int points) {}
