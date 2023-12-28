package com.btb.odj.model.mongodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.UUID;

@Data
@Builder
@Document("Driver")
@AllArgsConstructor
public class M_Driver {

    @Id
    private ObjectId id;
    @Indexed
    private UUID refId;

    private String name;
    private String country;
    private int points;

    @DocumentReference(lazy = true)
    private M_Team team;
}
