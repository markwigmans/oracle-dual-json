package com.btb.odj.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@NoArgsConstructor
public abstract class AbstractEntity implements Serializable {

    @Id
    private int id;
}
