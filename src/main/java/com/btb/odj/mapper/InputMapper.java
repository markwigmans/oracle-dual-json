package com.btb.odj.mapper;

import com.btb.odj.model.Data_Race;
import com.btb.odj.model.elasticsearch.E_InputDocument;
import com.btb.odj.model.jpa.Data_InputDocument;
import com.btb.odj.model.jpa.J_InputDocument;
import com.btb.odj.model.mongodb.M_InputDocument;
import com.btb.odj.repository.elasticsearch.E_InputDocumentRepository;
import com.btb.odj.repository.jpa.Data_InputDocumentRepository;
import com.btb.odj.repository.mongodb.M_InputDocumentRepository;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Mapper(
        config = Config.class,
        uses = {TeamMapper.class, PodiumPositionMapper.class})
@Slf4j
public abstract class InputMapper {

    @Autowired
    protected E_InputDocumentRepository esRepository;

    @Autowired
    protected M_InputDocumentRepository mongodbRepository;

    @Autowired
    protected Data_InputDocumentRepository jpaRepository;

    @AfterMapping
    protected void fillId(Data_Race source, @MappingTarget E_InputDocument.E_InputDocumentBuilder target) {
        log.debug("find ID for: {}", target);
        Optional<E_InputDocument> document = esRepository.findByRefId(source.getId());
        document.ifPresent(e -> target.id(e.getId()));
    }

    @AfterMapping
    protected void fillId(Data_Race source, @MappingTarget M_InputDocument.M_InputDocumentBuilder target) {
        log.debug("find ID for: {}", target);
        Optional<M_InputDocument> document = mongodbRepository.findByRefId(Utils.IDtoString(source));
        document.ifPresent(e -> target.id(e.getId()));
    }

    @AfterMapping
    protected void fillId(Data_Race source, @MappingTarget J_InputDocument.J_InputDocumentBuilder target) {
        log.debug("find ID for: {}", target);
        Optional<Data_InputDocument> document = jpaRepository.findByRefId(Utils.IDtoString(source));
        document.ifPresent(e -> target.id(e.getId()));
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "refId", source = "id")
    public abstract E_InputDocument from_Data_to_E(Data_Race source);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "refId", source = "id")
    public abstract M_InputDocument from_Data_to_M(Data_Race source);

    @Mapping(target = "refId", source = "id")
    public abstract J_InputDocument from_Data_to_J(Data_Race source);
}
