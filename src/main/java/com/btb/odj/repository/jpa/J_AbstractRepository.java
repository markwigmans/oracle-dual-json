package com.btb.odj.repository.jpa;

import com.btb.odj.model.jpa.J_AbstractEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.UUID;

@NoRepositoryBean
public interface J_AbstractRepository<T extends J_AbstractEntity> extends JpaRepository<T, UUID> {

}
