package com.btb.odj.repository.elasticsearch;

import com.btb.odj.model.elasticsearch.E_InputDocument;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface E_InputDocumentRepository extends ElasticsearchRepository<E_InputDocument, String> {

    Optional<E_InputDocument> findByRefId(UUID refId);
}
