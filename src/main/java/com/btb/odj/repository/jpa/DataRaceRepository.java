package com.btb.odj.repository.jpa;

import com.btb.odj.model.Data_Driver;
import com.btb.odj.model.Data_PodiumPosition;
import com.btb.odj.model.Data_Race;
import java.util.Collection;
import org.springframework.data.jpa.repository.Query;

public interface DataRaceRepository extends DataAbstractRepository<Data_Race> {

    @Query("select pp from Data_PodiumPosition pp where pp.driver = ?1")
    Collection<Data_PodiumPosition> findRaces(Data_Driver driver);
}
