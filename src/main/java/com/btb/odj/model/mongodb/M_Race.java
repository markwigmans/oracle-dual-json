package com.btb.odj.model.mongodb;

import lombok.Builder;

import java.util.Date;

@Builder
public record M_Race(
     String refId,
     String name,
     String country,
     int laps,
     Date raceDate){
}

