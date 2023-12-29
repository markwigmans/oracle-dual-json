package com.btb.odj.model.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "OutputDocument")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Immutable
public class J_OutputDocument extends J_AbstractEntity {

    private String data;
}
