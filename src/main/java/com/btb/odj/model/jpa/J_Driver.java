package com.btb.odj.model.jpa;

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
public class J_Driver extends J_AbstractEntity {

    private String name;
    private String country;
    private int points;

    @ManyToOne
    @ToStringExclude
    private J_Team team;
}
