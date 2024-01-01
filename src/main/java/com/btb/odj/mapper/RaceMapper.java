package com.btb.odj.mapper;

import com.btb.odj.model.Data_Race;
import com.btb.odj.model.elasticsearch.E_Race;
import com.btb.odj.model.jpa.J_Race;
import com.btb.odj.model.mongodb.M_Race;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = Config.class)
public interface RaceMapper {

    @Mapping(target = "refId", source = "id")
    M_Race from_Data_to_M(Data_Race race);

    @Mapping(target = "refId", source = "id")
    E_Race from_Data_to_E(Data_Race race);

    @Mapping(target = "refId", source = "id")
    J_Race from_Data_to_J(Data_Race race);
}
