package com.btb.odj.mapper;

import com.btb.odj.model.elasticsearch.E_Team;
import com.btb.odj.model.jpa.J_Team;
import com.btb.odj.model.mongodb.M_Team;
import com.btb.odj.repository.mongodb.M_TeamRepository;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Mapper(config = Config.class, uses = DriverMapper.class)
@Slf4j
@Named("TeamMapper")
public abstract class TeamMapper {

    @Autowired
    protected M_TeamRepository teamRepository;

    @AfterMapping
    protected void fillId(J_Team source, @MappingTarget M_Team.M_TeamBuilder target) {
        Optional<M_Team> team = teamRepository.findM_TeamByRefId(source.getId());
        log.info("find ID for: {}, found:{}", target, team);
        team.ifPresent(e -> target.id(e.getId()));
    }

    @Named("Full")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "refId", source = "id")
    public abstract M_Team transform_J_to_M(J_Team team);

    @Named("Minimal")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "refId", source = "id")
    @Mapping(target = "drivers", ignore = true)
    public abstract M_Team minimal_J_to_M(J_Team team);

    @Mapping(target = "refId", source = "id")
    public abstract E_Team from_J_to_E(J_Team team);
}
