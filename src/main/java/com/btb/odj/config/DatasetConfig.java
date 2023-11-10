package com.btb.odj.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@ConfigurationProperties(prefix = "data")
public class DatasetConfig {
    private double teamsMultiplier = 2.0;
    private double driversMultiplier = 2.0;

    private int driverMaxPoints = 100;

    private int raceMinLaps = 40;
    private int raceMaxLaps = 80;
    private int racePreviousDays = 365;

    private int teamMaxPoints = 400;
    private double minDriversMultiplier = 0.2;
}

