package com.btb.odj.model.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.hibernate.annotations.Immutable;

/**
 *
 */
@Entity
@Table(name = "PodiumPosition", indexes = @Index(name = "idx_driver", columnList = "driver_id"))
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Immutable
public class J_PodiumPosition extends J_AbstractEntity {

    @ManyToOne
    @ToStringExclude
    private J_Race race;

    @ManyToOne
    @ToStringExclude
    private J_Driver driver;

    private int points;
    private int position;
}
