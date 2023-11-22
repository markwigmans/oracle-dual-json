package com.btb.odj.service;

import com.btb.odj.model.jpa.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface TeamRepository extends JpaRepository<Team, UUID> {

    @Modifying
    @Query(value = """
            UPDATE TEAM SET TEAM.POINTS = (
             SELECT sum(pp.POINTS)
             FROM PODIUM_POSITION pp
             JOIN DRIVER d on d.ID = pp.DRIVER_ID
                JOIN team t on t.ID = d.TEAM_ID
                WHERE TEAM.ID = t.ID)
            WHERE team.ID IN (SELECT DISTINCT TEAM_ID
             FROM team
             JOIN DRIVER d ON d.TEAM_ID = team.ID
             JOIN PODIUM_POSITION pp ON pp.DRIVER_ID = d.ID)""", nativeQuery = true)
    void updatePoints();
}
