package com.btb.odj.repository;

import com.btb.odj.model.jpa.AbstractEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface AbstractRepository<T extends AbstractEntity> extends JpaRepository<T, UUID> {

    @EntityGraph(value = AbstractEntity.EAGER_ALL, type = EntityGraph.EntityGraphType.LOAD)
    Optional<T> findEagerById(UUID id);
}
