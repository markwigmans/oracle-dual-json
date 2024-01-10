package com.btb.odj.model.jpa;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder(toBuilder = true)
public record J_OutputDocument(UUID id, UUID refId, J_Driver driver, List<J_PodiumPosition> podium) {}
