package com.btb.odj.mapper;

import com.btb.odj.model.jpa.J_Team;
import com.btb.odj.model.mongodb.M_Team;
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
public abstract class TeamMapper {

    @Autowired
    protected M_TeamRepository teamRepository;

    @AfterMapping
    protected void fillId(J_Team source, @MappingTarget M_Team.M_TeamBuilder target) {
        Optional<M_Team> team = teamRepository.findM_TeamByRefId(source.getId());
        log.info("find ID for: {}, found:{}", target, team);
        team.ifPresent(e -> target.id(e.getId()));
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "refId", source = "id")
    public abstract M_Team transform_J_to_M(J_Team team);

//    @Named("MinimalTeam")
//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "refId", source = "id")
//    @Mapping(target = "drivers", ignore = true)
//    public abstract M_Team minimal_J_to_M(J_Team team);
}
