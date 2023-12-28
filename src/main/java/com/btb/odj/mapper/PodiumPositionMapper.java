package com.btb.odj.mapper;

import com.btb.odj.model.elasticsearch.E_PodiumPosition;
import com.btb.odj.model.jpa.J_PodiumPosition;
import com.btb.odj.model.mongodb.M_PodiumPosition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = Config.class, uses = { TeamMapper.class, RaceMapper.class } )
public interface PodiumPositionMapper {

    @Mapping(target = "team", source = "driver.team", qualifiedByName = {"TeamMapper", "Minimal"})
    M_PodiumPosition transform_J_to_M(J_PodiumPosition podiumPosition);

    E_PodiumPosition from_J_to_E(J_PodiumPosition podiumPosition);
}
