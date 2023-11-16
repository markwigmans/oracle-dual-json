package com.btb.odj.model.jpa;

import jakarta.persistence.CascadeType;
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
public class PodiumPosition extends AbstractEntity {

    @ManyToOne(cascade = CascadeType.ALL)
    @ToStringExclude
    private Race race;

    @ManyToOne(cascade = CascadeType.ALL)
    @ToStringExclude
    private Driver driver;

    private int position;
}
