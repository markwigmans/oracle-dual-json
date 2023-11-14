package com.btb.odj.model.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringExclude;

/**
 *
 */
@Entity
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Driver extends AbstractEntity {

    private String name;
    private String country;
    private int points;

    @JsonIgnore
    @ManyToOne
    @ToStringExclude
    private Team team;
}
