package com.btb.odj.model.jpa;

import com.btb.odj.annotation.UuidV7Generator;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@MappedSuperclass
@NoArgsConstructor
@Getter
public abstract class J_AbstractEntity implements Serializable {

    @Id
    @UuidV7Generator
    private UUID id;
}
