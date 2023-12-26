package com.btb.odj.mapper;

import com.btb.odj.model.jpa.J_Race;
import com.btb.odj.model.mongodb.M_Race;
import com.btb.odj.repository.mongodb.M_RaceRepository;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Mapper(config = Config.class, uses = PodiumPositionMapper.class)
public abstract class RaceMapper {

    @Autowired
    protected M_RaceRepository raceRepository;

    @AfterMapping
    protected void fillId(J_Race source, @MappingTarget M_Race.M_RaceBuilder target) {
        Optional<M_Race> race = raceRepository.findM_RaceByRefId (source.getId());
        race.ifPresent(e -> target.id(e.getId()));
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "refId", source = "id")
    public abstract M_Race transform_J_to_M(J_Race race);
}
