package com.btb.odj.model.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringExclude;

import java.util.List;

@Entity
@Table(name = "Team")
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class J_Team extends J_AbstractEntity {

    private String name;
    private String city;
    private String streetName;

    @Column(name = "streetNumber", length = 20)
    private String number;

    private String country;
    private int points;

    @OneToMany(mappedBy = "team")
    @ToStringExclude
    private List<J_Driver> drivers;
}
