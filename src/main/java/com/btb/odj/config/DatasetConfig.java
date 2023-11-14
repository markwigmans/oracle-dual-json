package com.btb.odj.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@ConfigurationProperties(prefix = "data")
public class DatasetConfig {
    private final double teamsMultiplier = 2.0;
    private final int maxDrivers = 4;

    private final int raceMinLaps = 40;
    private final int raceMaxLaps = 80;
    private final int racePreviousDays = 365;
}

