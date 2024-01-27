package com.btb.odj.model.elasticsearch;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;
import java.util.UUID;

/**
 *
 */
@Document(indexName = "output")
@Data
@Builder
public class E_OutputDocument {

    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private UUID refId;

    private E_Driver driver;
    private List<E_PodiumPosition> podium;
}
