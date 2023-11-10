package com.btb.odj.service;

import com.btb.odj.model.jpa.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
