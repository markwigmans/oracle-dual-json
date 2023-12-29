package com.btb.odj.mapper;

import com.btb.odj.model.elasticsearch.E_OutputDocument;
import com.btb.odj.model.elasticsearch.E_PodiumPosition;
import com.btb.odj.model.jpa.J_Driver;
import com.btb.odj.model.jpa.J_PodiumPosition;
import com.btb.odj.model.mongodb.M_OutputDocument;
import com.btb.odj.model.mongodb.M_PodiumPosition;
import com.btb.odj.repository.elasticsearch.E_OutputDocumentRepository;
import com.btb.odj.repository.mongodb.M_OutputDocumentRepository;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        config = Config.class,
        uses = {TeamMapper.class, RaceMapper.class})
@Slf4j
@DecoratedWith(OutputDecorator.class)
public abstract class OutputMapper {

    @Autowired
    protected E_OutputDocumentRepository esRepository;

    @Autowired
    protected M_OutputDocumentRepository mongodbRepository;

    @AfterMapping
    protected void fillId(J_Driver source, @MappingTarget E_OutputDocument.E_OutputDocumentBuilder target) {
        log.debug("find ID for: {}", target);
        Optional<E_OutputDocument> outputDocument = esRepository.findByRefId(source.getId());
        outputDocument.ifPresent(e -> target.id(e.getId()));
    }

    @AfterMapping
    protected void fillId(J_Driver source, @MappingTarget M_OutputDocument.M_OutputDocumentBuilder target) {
        log.debug("find ID for: {}", target);
        Optional<M_OutputDocument> outputDocument = mongodbRepository.findByRefId(Utils.IDtoString(source));
        outputDocument.ifPresent(e -> target.id(e.getId()));
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "refId", source = "id")
    @Mapping(target = "driver", source = "source")
    @Mapping(target = "podium", ignore = true)
    public abstract E_OutputDocument from_J_to_E(J_Driver source);

    @Mapping(target = "driver", ignore = true)
    abstract E_PodiumPosition from_J_to_E_Minimal(J_PodiumPosition podiumPosition);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "refId", source = "id")
    @Mapping(target = "driver", source = "source")
    @Mapping(target = "podium", ignore = true)
    public abstract M_OutputDocument from_J_to_M(J_Driver source);

    @Mapping(target = "driver", ignore = true)
    abstract M_PodiumPosition from_J_to_M_Minimal(J_PodiumPosition podiumPosition);
}
