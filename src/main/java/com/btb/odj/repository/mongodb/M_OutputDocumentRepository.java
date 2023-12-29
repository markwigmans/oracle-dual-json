package com.btb.odj.repository.mongodb;

import com.btb.odj.model.mongodb.M_OutputDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface M_OutputDocumentRepository extends MongoRepository<M_OutputDocument, String> {

    Optional<M_OutputDocument> findByRefId(String refId);

}
