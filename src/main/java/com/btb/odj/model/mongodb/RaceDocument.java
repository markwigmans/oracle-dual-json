package com.btb.odj.model.mongodb;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class RaceDocument {

    @Id
    private String id;

    private String data;
}
