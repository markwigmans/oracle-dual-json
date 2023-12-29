package com.btb.odj.repository.mongodb;

import com.btb.odj.model.mongodb.M_InputDocument;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface M_InputDocumentRepository extends MongoRepository<M_InputDocument, String> {

    Optional<M_InputDocument> findByRefId(String refId);
}
