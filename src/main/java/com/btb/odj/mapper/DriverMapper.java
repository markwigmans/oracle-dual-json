package com.btb.odj.mapper;

import com.btb.odj.model.elasticsearch.E_Driver;
import com.btb.odj.model.jpa.J_Driver;
import com.btb.odj.model.mongodb.M_Driver;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = Config.class)
public interface DriverMapper {

    @Mapping(target = "refId", source = "id")
    @Mapping(target = "team.refId", source = "team.id")
    M_Driver from_J_to_M(J_Driver driver);

    @Mapping(target = "refId", source = "id")
    @Mapping(target = "team.refId", source = "team.id")
    E_Driver from_J_to_E(J_Driver driver);
}
