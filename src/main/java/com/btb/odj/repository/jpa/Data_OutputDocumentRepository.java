package com.btb.odj.repository.jpa;

import com.btb.odj.model.jpa.Data_OutputDocument;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

public interface Data_OutputDocumentRepository extends DataAbstractRepository<Data_OutputDocument> {

    // query using refId field from json structure
    @Query(value = "select doc.* from Output_Document doc where doc.json.refId = ?1", nativeQuery = true)
    Optional<Data_OutputDocument> findByRefId(String refId);
}
