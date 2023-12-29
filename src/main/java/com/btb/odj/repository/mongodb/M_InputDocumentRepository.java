package com.btb.odj.repository.mongodb;

import com.btb.odj.model.mongodb.M_InputDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface M_InputDocumentRepository extends MongoRepository<M_InputDocument, String> {

    Optional<M_InputDocument> findByRefId(String refId);

}
