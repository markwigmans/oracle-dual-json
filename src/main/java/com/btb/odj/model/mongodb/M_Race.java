package com.btb.odj.model.mongodb;

import java.util.Date;
import lombok.Builder;

@Builder
public record M_Race(String refId, String name, String country, int laps, Date raceDate) {}
