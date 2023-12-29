package com.btb.odj.mapper;

import com.btb.odj.model.elasticsearch.E_Race;
import com.btb.odj.model.jpa.J_Race;
import com.btb.odj.model.mongodb.M_Race;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = Config.class)
public interface RaceMapper {

    @Mapping(target = "refId", source = "id")
    M_Race from_J_to_M(J_Race race);

    @Mapping(target = "refId", source = "id")
    E_Race from_J_to_E(J_Race race);
}
