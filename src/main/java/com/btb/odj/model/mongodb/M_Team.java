package com.btb.odj.model.mongodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@Document("Team")
@AllArgsConstructor
public class M_Team {

    @Id
    private String id;
    private UUID refId;

    private String name;
    private String city;
    private String streetName;
    private String number;
    private String country;
    private int points;

    @DocumentReference
    private List<M_Driver> drivers;
}
