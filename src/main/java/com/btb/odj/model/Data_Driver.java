package com.btb.odj.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringExclude;

@Entity
@Table(name = "Driver")
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Data_Driver extends Data_AbstractEntity {

    private String name;
    private String country;
    private int points;

    @ManyToOne
    @ToStringExclude
    private Data_Team team;
}
