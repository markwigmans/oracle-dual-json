package com.btb.odj.mapper;

import com.btb.odj.model.Data_PodiumPosition;
import com.btb.odj.model.elasticsearch.E_PodiumPosition;
import com.btb.odj.model.jpa.J_PodiumPosition;
import com.btb.odj.model.mongodb.M_PodiumPosition;
import org.mapstruct.Mapper;

@Mapper(
        config = Config.class,
        uses = {TeamMapper.class, RaceMapper.class})
public interface PodiumPositionMapper {

    M_PodiumPosition from_Data_to_M(Data_PodiumPosition podiumPosition);

    E_PodiumPosition from_Data_to_E(Data_PodiumPosition podiumPosition);

    J_PodiumPosition from_Data_to_J(Data_PodiumPosition podiumPosition);
}
