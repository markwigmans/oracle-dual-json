package com.btb.odj.model.jpa;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringExclude;

import java.util.List;

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
public class Team extends AbstractEntity {

    private String name;
    private String city;
    private String streetName;
    @Column(name = "streetNumber", length = 20)
    private String number;
    private String country;

    private int points;

    @OneToMany(cascade = CascadeType.ALL)
    @ToStringExclude
    private List<Driver> drivers;
}
