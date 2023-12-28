package com.btb.odj.repository.elasticsearch;

import com.btb.odj.model.elasticsearch.E_OutputDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface E_OutputDocumentRepository extends ElasticsearchRepository<E_OutputDocument, String> {

    Optional<E_OutputDocument> findByRefId(UUID refId);

}
