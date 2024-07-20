package com.btb.odj.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Table(name = "Race")
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Data_Race extends Data_AbstractEntity {

    private String name;
    private String country;
    private int laps;
    private Date raceDate;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "race")
    @ToStringExclude
    @BatchSize(size = 10)
    @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    private List<Data_PodiumPosition> podium;
}
