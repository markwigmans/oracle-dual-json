package com.btb.odj.model.jpa;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@MappedSuperclass
@NoArgsConstructor
public abstract class AbstractEntity implements Serializable {

    @Id
    private int id;
}
