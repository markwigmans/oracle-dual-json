package com.btb.odj.mapper;

import com.btb.odj.model.Data_Team;
import com.btb.odj.model.elasticsearch.E_Team;
import com.btb.odj.model.jpa.J_Team;
import com.btb.odj.model.mongodb.M_Team;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = Config.class, uses = DriverMapper.class)
public interface TeamMapper {

    @Mapping(target = "refId", source = "id")
    M_Team from_Data_to_M(Data_Team team);

    @Mapping(target = "refId", source = "id")
    E_Team from_Data_to_E(Data_Team team);

    @Mapping(target = "refId", source = "id")
    J_Team from_Data_to_J(Data_Team team);
}
