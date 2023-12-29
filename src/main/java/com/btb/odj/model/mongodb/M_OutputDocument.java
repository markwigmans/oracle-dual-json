package com.btb.odj.model.mongodb;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 */
@Document("Output")
@Data
@Builder
public class M_OutputDocument {

    @Id
    private String id;

    @Indexed
    private String refId;

    private M_Driver driver;
    private List<M_PodiumPosition> podium;
}
