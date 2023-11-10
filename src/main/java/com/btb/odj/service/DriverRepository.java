package com.btb.odj.service;

import com.btb.odj.model.jpa.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Long> {
}
