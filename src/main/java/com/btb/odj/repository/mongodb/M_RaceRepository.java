package com.btb.odj.repository.mongodb;

import com.btb.odj.model.mongodb.M_Race;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface M_RaceRepository extends MongoRepository<M_Race, String> {

    Optional<M_Race> findM_RaceByRefId(UUID refId);
}
