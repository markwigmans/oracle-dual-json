package com.btb.odj.service;

import com.btb.odj.model.jpa.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface DriverRepository extends JpaRepository<Driver, UUID> {

    @Query(value = "select * from driver sample(10) FETCH FIRST ?1 ROWS ONLY ", nativeQuery = true)
    List<Driver> findRandomDrivers(Integer limit);

    @Modifying
    @Query(value = "UPDATE DRIVER d SET d.POINTS = COALESCE((SELECT sum(pp.POINTS) FROM PODIUM_POSITION pp WHERE pp.DRIVER_ID = d.ID),0)", nativeQuery = true)
    void updatePoints();
}
