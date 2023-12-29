package com.btb.odj.mapper;

import com.btb.odj.model.elasticsearch.E_OutputDocument;
import com.btb.odj.model.jpa.J_Driver;
import com.btb.odj.model.jpa.J_PodiumPosition;
import com.btb.odj.model.mongodb.M_OutputDocument;
import com.btb.odj.repository.jpa.J_RaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Collection;

public abstract class OutputDecorator extends OutputMapper {

    @Autowired
    @Qualifier("delegate")
    private OutputMapper delegate;
    @Autowired
    private J_RaceRepository repository;

    @Override
    public E_OutputDocument from_J_to_E(J_Driver driver) {
        E_OutputDocument dto = delegate.from_J_to_E(driver);
        Collection<J_PodiumPosition> races = repository.findRaces(driver);
        dto.setPodium(races.stream().map(this::from_J_to_E_Minimal).toList());
        return dto;
    }

    @Override
    public M_OutputDocument from_J_to_M(J_Driver driver) {
        M_OutputDocument dto = delegate.from_J_to_M(driver);
        Collection<J_PodiumPosition> races = repository.findRaces(driver);
        dto.setPodium(races.stream().map(this::from_J_to_M_Minimal).toList());
        return dto;
    }

}
