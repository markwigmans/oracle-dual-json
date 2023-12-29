package com.btb.odj.model.elasticsearch;

import java.util.UUID;
import lombok.Builder;

@Builder
public record E_Team(
        UUID refId, String name, String city, String streetName, String number, String country, int points) {}
