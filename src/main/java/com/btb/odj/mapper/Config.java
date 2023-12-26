package com.btb.odj.mapper;

import org.mapstruct.MapperConfig;
import org.mapstruct.MappingConstants;

@MapperConfig(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = DriverMapper.class
)
public interface Config {
}
