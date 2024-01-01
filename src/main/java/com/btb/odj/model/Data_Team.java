package com.btb.odj.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringExclude;

@Entity
@Table(name = "Team")
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Data_Team extends Data_AbstractEntity {

    private String name;
    private String city;
    private String streetName;

    @Column(name = "streetNumber", length = 20)
    private String number;

    private String country;
    private int points;

    @OneToMany(mappedBy = "team")
    @ToStringExclude
    private List<Data_Driver> drivers;
}
