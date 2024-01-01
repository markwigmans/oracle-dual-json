package com.btb.odj.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "PodiumPosition", indexes = @Index(name = "idx_driver", columnList = "driver_id"))
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Immutable
public class Data_PodiumPosition extends Data_AbstractEntity {

    @ManyToOne
    @ToStringExclude
    private Data_Race race;

    @ManyToOne
    @ToStringExclude
    private Data_Driver driver;

    private int points;
    private int position;
}
