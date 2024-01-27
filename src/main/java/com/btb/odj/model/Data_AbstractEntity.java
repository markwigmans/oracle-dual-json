package com.btb.odj.model;

import com.btb.odj.annotation.UuidV7Generator;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.UUID;

@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@Getter
public abstract class Data_AbstractEntity implements Serializable {

    @Id
    @UuidV7Generator
    private UUID id;
}
