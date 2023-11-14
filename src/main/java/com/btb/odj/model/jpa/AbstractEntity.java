package com.btb.odj.model.jpa;

import com.btb.odj.annotation.UuidV7Generator;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@MappedSuperclass
@NoArgsConstructor
@Getter
public abstract class AbstractEntity implements Serializable {

    @Id
    @UuidV7Generator
    private UUID id;
}
