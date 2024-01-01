package com.btb.odj.mapper;

import com.btb.odj.model.Data_Driver;
import com.btb.odj.model.Data_PodiumPosition;
import com.btb.odj.model.elasticsearch.E_OutputDocument;
import com.btb.odj.model.jpa.J_OutputDocument;
import com.btb.odj.model.mongodb.M_OutputDocument;
import com.btb.odj.repository.jpa.DataRaceRepository;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class OutputDecorator extends OutputMapper {

    @Autowired
    @Qualifier("delegate")
    private OutputMapper delegate;

    @Autowired
    private DataRaceRepository raceRepository;

    @Override
    public E_OutputDocument from_Data_to_E(Data_Driver driver) {
        E_OutputDocument dto = delegate.from_Data_to_E(driver);
        Collection<Data_PodiumPosition> races = raceRepository.findRaces(driver);
        dto.setPodium(races.stream().map(this::from_Data_to_E_Minimal).toList());
        return dto;
    }

    @Override
    public M_OutputDocument from_Data_to_M(Data_Driver driver) {
        M_OutputDocument dto = delegate.from_Data_to_M(driver);
        Collection<Data_PodiumPosition> races = raceRepository.findRaces(driver);
        dto.setPodium(races.stream().map(this::from_Data_to_M_Minimal).toList());
        return dto;
    }

    @Override
    public J_OutputDocument from_Data_to_J(Data_Driver driver) {
        J_OutputDocument.J_OutputDocumentBuilder builder = delegate.from_Data_to_J(driver).toBuilder();
        Collection<Data_PodiumPosition> races = raceRepository.findRaces(driver);
        builder.podium(races.stream().map(this::from_Data_to_J_Minimal).toList());
        return builder.build();
    }
}
