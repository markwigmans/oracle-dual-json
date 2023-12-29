package com.btb.odj.model.jpa;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringExclude;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Race")
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class J_Race extends J_AbstractEntity {

    private String name;
    private String country;
    private int laps;
    private Date raceDate;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "race")
    @ToStringExclude
    private List<J_PodiumPosition> podium;
}
