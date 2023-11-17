package com.btb.odj.service;

import com.btb.odj.model.jpa.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface DriverRepository extends JpaRepository<Driver, UUID> {

    @Query(value = "select * from driver order by dbms_random.value FETCH FIRST ?1 ROWS ONLY", nativeQuery = true)
    List<Driver> findRandomDrivers(Integer limit);
}
