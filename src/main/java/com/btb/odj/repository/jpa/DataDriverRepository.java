package com.btb.odj.repository.jpa;

import com.btb.odj.model.Data_Driver;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface DataDriverRepository extends DataAbstractRepository<Data_Driver> {

    @Modifying
    @Query(
            value =
                    """
            UPDATE DRIVER d
            SET d.POINTS = (SELECT sum(pp.POINTS) FROM PODIUM_POSITION pp WHERE pp.DRIVER_ID = d.ID)
            WHERE d.ID IN (SELECT DRIVER_ID FROM PODIUM_POSITION)""",
            nativeQuery = true)
    void updatePoints();
}
