package com.btb.odj.repository.mongodb;

import com.btb.odj.model.mongodb.M_Team;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface M_TeamRepository extends MongoRepository<M_Team, String> {

    Optional<M_Team> findM_TeamByRefId(UUID refId);
}
