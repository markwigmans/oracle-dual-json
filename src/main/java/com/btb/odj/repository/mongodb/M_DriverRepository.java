package com.btb.odj.repository.mongodb;

import com.btb.odj.model.mongodb.M_Driver;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface M_DriverRepository extends MongoRepository<M_Driver, String> {

    Optional<M_Driver> findM_DriverByRefId(UUID refId);
}
