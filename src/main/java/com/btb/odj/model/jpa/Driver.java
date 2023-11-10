package com.btb.odj.model.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

/**
 * CREATE TABLE driver
 *   (driver_id INTEGER GENERATED BY DEFAULT ON NULL AS IDENTITY,
 *    name      VARCHAR2(255) NOT NULL UNIQUE,
 *    points    INTEGER NOT NULL,
 *    team_id   INTEGER,
 *    CONSTRAINT driver_pk PRIMARY KEY(driver_id),
 *    CONSTRAINT driver_fk FOREIGN KEY(team_id) REFERENCES team(team_id));
 */
@Entity
@Builder
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Driver extends AbstractEntity {

    private String name;
    private int points;

    @JsonIgnore
    @ManyToOne
    private Team team;
}
