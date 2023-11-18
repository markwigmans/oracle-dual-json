package com.btb.odj.service;

import com.btb.odj.model.jpa.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface TeamRepository extends JpaRepository<Team, UUID> {

    @Modifying
   @Query(value = """
        UPDATE TEAM SET TEAM.POINTS = COALESCE((
            SELECT sum(pp.POINTS)
            FROM PODIUM_POSITION pp
            JOIN DRIVER d on d.ID = pp.DRIVER_ID
            JOIN team t on t.ID = d.TEAM_ID
            WHERE TEAM.ID = t.ID), 0)""", nativeQuery = true)
   void updatePoints();
}
