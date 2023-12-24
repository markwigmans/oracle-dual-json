package com.btb.odj.model.jpa;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
@Table(indexes = @Index(name = "idx_driver", columnList = "driver_id"))
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Immutable
@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class)
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
