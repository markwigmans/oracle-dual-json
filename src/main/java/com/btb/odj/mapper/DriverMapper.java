package com.btb.odj.mapper;

import com.btb.odj.model.elasticsearch.E_Driver;
import com.btb.odj.model.jpa.J_Driver;
import com.btb.odj.model.mongodb.M_Driver;
import com.btb.odj.model.mongodb.M_Driver.M_DriverBuilder;
import com.btb.odj.model.mongodb.M_Team;
import com.btb.odj.repository.mongodb.M_DriverRepository;
import com.btb.odj.repository.mongodb.M_TeamRepository;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Mapper(config = Config.class)
@Slf4j
public abstract class DriverMapper {

    @Autowired
    protected M_DriverRepository driverRepository;
    @Autowired
    protected M_TeamRepository teamRepository;

    @AfterMapping
    protected void fillId(J_Driver source, @MappingTarget M_DriverBuilder target) {
        log.debug("find ID for: {}", target);
        Optional<M_Driver> driver = driverRepository.findM_DriverByRefId(source.getId());
        driver.ifPresent(e -> target.id(e.getId()));
        Optional<M_Team> team = teamRepository.findM_TeamByRefId(source.getTeam().getId());
        team.ifPresent(target::team);
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "refId", source = "id")
    @Mapping(target = "team", ignore = true)
    public abstract M_Driver transform_J_to_M(J_Driver driver);

    @Mapping(target = "refId", source = "id")
    @Mapping(target = "team.refId", source = "team.id")
    public abstract E_Driver from_J_to_E(J_Driver driver);
}
