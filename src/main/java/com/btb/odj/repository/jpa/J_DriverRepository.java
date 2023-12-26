package com.btb.odj.repository.jpa;

import com.btb.odj.model.jpa.J_Driver;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface J_DriverRepository extends J_AbstractRepository<J_Driver> {

    @Modifying
    @Query(value = """
            UPDATE DRIVER d
            SET d.POINTS = (SELECT sum(pp.POINTS) FROM PODIUM_POSITION pp WHERE pp.DRIVER_ID = d.ID)
            WHERE d.ID IN (SELECT DRIVER_ID FROM PODIUM_POSITION)""", nativeQuery = true)
    void updatePoints();
}
