package com.btb.odj.model.mongodb;

import lombok.Builder;

@Builder
public record M_Team(
        String refId, String name, String city, String streetName, String number, String country, int points) {}
