package com.btb.odj.model.jpa;

import jakarta.persistence.Entity;
import lombok.*;

import java.util.Date;

/**
 * CREATE TABLE race
 * (race_id   INTEGER GENERATED BY DEFAULT ON NULL AS IDENTITY,
 * name      VARCHAR2(255) NOT NULL UNIQUE,
 * laps      INTEGER NOT NULL,
 * race_date DATE,
 * podium    JSON,
 * CONSTRAINT   race_pk PRIMARY KEY(race_id));
 */
@Entity
@Builder
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Race extends AbstractEntity {

    private String name;
    private int laps;
    private Date raceDate;

//    @Type(JsonType.class)
//    private String podium;
}
