package com.btb.odj.mapper;

import com.btb.odj.model.jpa.J_PodiumPosition;
import com.btb.odj.model.mongodb.M_PodiumPosition;
import org.mapstruct.Mapper;

@Mapper(config = Config.class, uses = TeamMapper.class)
public interface PodiumPositionMapper {

    //@Mapping(target = "team", source = "driver.team")
    M_PodiumPosition transform_J_to_M(J_PodiumPosition podiumPosition);
}
