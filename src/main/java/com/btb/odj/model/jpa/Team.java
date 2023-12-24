package com.btb.odj.model.jpa;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedEntityGraph;
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
@NamedEntityGraph(name = AbstractEntity.EAGER_ALL, includeAllAttributes = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class)
public class Team extends AbstractEntity {

    private String name;
    private String city;
    private String streetName;
    @Column(name = "streetNumber", length = 20)
    private String number;
    private String country;

    private int points;

    @OneToMany(mappedBy = "team")
    @ToStringExclude
    private List<Driver> drivers;
}
