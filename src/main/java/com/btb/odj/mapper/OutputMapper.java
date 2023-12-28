package com.btb.odj.mapper;

import com.btb.odj.model.elasticsearch.E_OutputDocument;
import com.btb.odj.model.jpa.J_Driver;
import com.btb.odj.repository.elasticsearch.E_OutputDocumentRepository;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Mapper(config = Config.class, uses = { TeamMapper.class})
@Slf4j
public abstract class OutputMapper {

    @Autowired
    protected E_OutputDocumentRepository esRepository;

    @AfterMapping
    protected void fillId(J_Driver source, @MappingTarget E_OutputDocument.E_OutputDocumentBuilder target) {
        log.debug("find ID for: {}", target);
        Optional<E_OutputDocument> outputDocument = esRepository.findByRefId(source.getId());
        outputDocument.ifPresent(e -> target.id(e.getId()));
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "refId", source = "id")
    @Mapping(target = "driver", source = "source")
    public abstract E_OutputDocument from_J_to_E(J_Driver source);
}
