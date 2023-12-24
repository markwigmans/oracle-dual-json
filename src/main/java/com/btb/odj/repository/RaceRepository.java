package com.btb.odj.repository;

import com.btb.odj.model.jpa.AbstractEntity;
import com.btb.odj.model.jpa.Race;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.Optional;
import java.util.UUID;

public interface RaceRepository extends AbstractRepository<Race> {

    @EntityGraph(value = AbstractEntity.EAGER_ALL, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Race> findEagerById(UUID id);
}
