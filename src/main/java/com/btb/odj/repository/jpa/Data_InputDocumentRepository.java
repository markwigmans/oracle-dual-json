package com.btb.odj.repository.jpa;

import com.btb.odj.model.jpa.Data_InputDocument;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

public interface Data_InputDocumentRepository extends DataAbstractRepository<Data_InputDocument> {

    // query using refId field from json structure
    @Query(value = "select doc.* from Input_Document doc where doc.json_data.refId = ?1", nativeQuery = true)
    Optional<Data_InputDocument> findByRefId(String refId);
}
