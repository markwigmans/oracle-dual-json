package com.btb.odj.mapper;

import com.btb.odj.model.elasticsearch.E_Team;
import com.btb.odj.model.jpa.J_Team;
import com.btb.odj.model.mongodb.M_Team;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = Config.class, uses = DriverMapper.class)
public interface TeamMapper {

    @Mapping(target = "refId", source = "id")
    M_Team from_J_to_M(J_Team team);

    @Mapping(target = "refId", source = "id")
    E_Team from_J_to_E(J_Team team);
}
