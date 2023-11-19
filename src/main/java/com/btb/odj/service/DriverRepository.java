package com.btb.odj.service;

import com.btb.odj.model.jpa.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface DriverRepository extends JpaRepository<Driver, UUID> {

    @Modifying
    @Query(value = """
            UPDATE DRIVER d
            SET d.POINTS = (SELECT sum(pp.POINTS) FROM PODIUM_POSITION pp WHERE pp.DRIVER_ID = d.ID)
            WHERE d.ID IN (SELECT DRIVER_ID FROM PODIUM_POSITION)""", nativeQuery = true)
    void updatePoints();
}
