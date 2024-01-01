package com.btb.odj.mapper;

import com.btb.odj.model.Data_Driver;
import com.btb.odj.model.elasticsearch.E_Driver;
import com.btb.odj.model.jpa.J_Driver;
import com.btb.odj.model.mongodb.M_Driver;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = Config.class)
public interface DriverMapper {

    @Mapping(target = "refId", source = "id")
    @Mapping(target = "team.refId", source = "team.id")
    M_Driver from_Data_to_M(Data_Driver driver);

    @Mapping(target = "refId", source = "id")
    @Mapping(target = "team.refId", source = "team.id")
    E_Driver from_Data_to_E(Data_Driver driver);

    @Mapping(target = "refId", source = "id")
    @Mapping(target = "team.refId", source = "team.id")
    J_Driver from_Data_to_J(Data_Driver driver);
}
