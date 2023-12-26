package com.btb.odj.model.mongodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@Builder
@Document("Driver")
@AllArgsConstructor
public class M_Driver {

    @Id
    private String id;
    private UUID refId;

    private String name;
    private String country;
    private int points;

//    @DocumentReference
//    private M_Team team;
}
