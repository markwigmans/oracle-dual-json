package com.btb.odj.model.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.hibernate.annotations.Immutable;

/**
 *
 */
@Entity
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Immutable
public class PodiumPosition extends AbstractEntity {

    @ManyToOne
    @ToStringExclude
    private Race race;

    @ManyToOne
    @ToStringExclude
    private Driver driver;

    private int points;
    private int position;
}
