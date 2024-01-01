package com.btb.odj.mapper;

import com.btb.odj.model.Data_Driver;
import com.btb.odj.model.Data_PodiumPosition;
import com.btb.odj.model.elasticsearch.E_OutputDocument;
import com.btb.odj.model.elasticsearch.E_PodiumPosition;
import com.btb.odj.model.jpa.Data_OutputDocument;
import com.btb.odj.model.jpa.J_OutputDocument;
import com.btb.odj.model.jpa.J_PodiumPosition;
import com.btb.odj.model.mongodb.M_OutputDocument;
import com.btb.odj.model.mongodb.M_PodiumPosition;
import com.btb.odj.repository.elasticsearch.E_OutputDocumentRepository;
import com.btb.odj.repository.jpa.Data_OutputDocumentRepository;
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

    @Autowired
    protected Data_OutputDocumentRepository jpaRepository;

    @AfterMapping
    protected void fillId(Data_Driver source, @MappingTarget E_OutputDocument.E_OutputDocumentBuilder target) {
        log.debug("find ID for: {}", target);
        Optional<E_OutputDocument> document = esRepository.findByRefId(source.getId());
        document.ifPresent(e -> target.id(e.getId()));
    }

    @AfterMapping
    protected void fillId(Data_Driver source, @MappingTarget M_OutputDocument.M_OutputDocumentBuilder target) {
        log.debug("find ID for: {}", target);
        Optional<M_OutputDocument> document = mongodbRepository.findByRefId(Utils.IDtoString(source));
        document.ifPresent(e -> target.id(e.getId()));
    }

    @AfterMapping
    protected void fillId(Data_Driver source, @MappingTarget J_OutputDocument.J_OutputDocumentBuilder target) {
        log.debug("find ID for: {}", target);
        Optional<Data_OutputDocument> document = jpaRepository.findByRefId(Utils.IDtoString(source));
        document.ifPresent(e -> target.id(e.getId()));
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "refId", source = "id")
    @Mapping(target = "driver", source = "source")
    @Mapping(target = "podium", ignore = true)
    public abstract E_OutputDocument from_Data_to_E(Data_Driver source);

    @Mapping(target = "driver", ignore = true)
    abstract E_PodiumPosition from_Data_to_E_Minimal(Data_PodiumPosition podiumPosition);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "refId", source = "id")
    @Mapping(target = "driver", source = "source")
    @Mapping(target = "podium", ignore = true)
    public abstract M_OutputDocument from_Data_to_M(Data_Driver source);

    @Mapping(target = "driver", ignore = true)
    abstract M_PodiumPosition from_Data_to_M_Minimal(Data_PodiumPosition podiumPosition);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "refId", source = "id")
    @Mapping(target = "driver", source = "source")
    @Mapping(target = "podium", ignore = true)
    public abstract J_OutputDocument from_Data_to_J(Data_Driver source);

    @Mapping(target = "driver", ignore = true)
    abstract J_PodiumPosition from_Data_to_J_Minimal(Data_PodiumPosition podiumPosition);
}
