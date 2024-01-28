package com.btb.odj.model.jpa;

import com.btb.odj.model.Data_AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "InputDocument")
@SuperBuilder
@NoArgsConstructor
public class Data_InputDocument extends Data_AbstractEntity {

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "json_data", nullable = false)
    private String json;
}
