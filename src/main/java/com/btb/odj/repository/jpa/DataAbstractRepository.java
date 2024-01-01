package com.btb.odj.repository.jpa;

import com.btb.odj.model.Data_AbstractEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface DataAbstractRepository<T extends Data_AbstractEntity> extends JpaRepository<T, UUID> {}
