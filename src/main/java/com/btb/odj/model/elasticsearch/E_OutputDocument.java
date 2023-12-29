package com.btb.odj.model.elasticsearch;

import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 *
 */
@Document(indexName = "output")
@Data
@Builder
public class E_OutputDocument {

    @Id
    private String id;

    private UUID refId;

    private E_Driver driver;
    private List<E_PodiumPosition> podium;
}
