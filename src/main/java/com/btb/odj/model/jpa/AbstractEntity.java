package com.btb.odj.model.jpa;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.util.UUID;

@MappedSuperclass
@NoArgsConstructor
public abstract class AbstractEntity implements Serializable {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    // TODO generate UUID v7
    private UUID id;
}
