package com.btb.odj.model.elasticsearch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.UUID;

/**
 *
 */

@Document(indexName = "output")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class E_OutputDocument {

    @Id
    private String id;
    private UUID refId;

    private E_Driver driver;
}
