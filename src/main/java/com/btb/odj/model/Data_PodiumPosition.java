package com.btb.odj.model;

import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Table(name = "PodiumPosition", indexes = @Index(name = "idx_driver", columnList = "driver_id"))
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
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
