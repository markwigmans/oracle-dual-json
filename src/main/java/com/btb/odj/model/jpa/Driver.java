package com.btb.odj.model.jpa;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class)
public class Driver extends AbstractEntity {

    private String name;
    private String country;
    private int points;

    @ManyToOne
    @ToStringExclude
    private Team team;
}
