
package com.btb.odj.mapper;

import com.btb.odj.model.elasticsearch.E_InputDocument;
import com.btb.odj.model.jpa.J_Race;
import com.btb.odj.model.mongodb.M_InputDocument;
import com.btb.odj.repository.elasticsearch.E_InputDocumentRepository;
import com.btb.odj.repository.mongodb.M_InputDocumentRepository;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Mapper(config = Config.class, uses = { TeamMapper.class, PodiumPositionMapper.class})
@Slf4j
public abstract class InputMapper {

    @Autowired
    protected E_InputDocumentRepository esRepository;
    @Autowired
    protected M_InputDocumentRepository mongodbRepository;


    @AfterMapping
    protected void fillId(J_Race source, @MappingTarget E_InputDocument.E_InputDocumentBuilder target) {
        log.debug("find ID for: {}", target);
        Optional<E_InputDocument> document = esRepository.findByRefId(source.getId());
        document.ifPresent(e -> target.id(e.getId()));
    }

    @AfterMapping
    protected void fillId(J_Race source, @MappingTarget M_InputDocument.M_InputDocumentBuilder target) {
        log.debug("find ID for: {}", target);
        Optional<M_InputDocument> document = mongodbRepository.findByRefId(Utils.IDtoString(source));
        document.ifPresent(e -> target.id(e.getId()));
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "refId", source = "id")
    public abstract E_InputDocument from_J_to_E(J_Race source);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "refId", source = "id")
    public abstract M_InputDocument from_J_to_M(J_Race source);
}
