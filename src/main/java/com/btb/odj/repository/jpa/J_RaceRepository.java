package com.btb.odj.repository.jpa;

import com.btb.odj.model.jpa.J_Driver;
import com.btb.odj.model.jpa.J_PodiumPosition;
import com.btb.odj.model.jpa.J_Race;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface J_RaceRepository extends J_AbstractRepository<J_Race> {

    @Query("select pp from J_PodiumPosition pp where pp.driver = ?1")
    Collection<J_PodiumPosition> findRaces(J_Driver driver);

}
