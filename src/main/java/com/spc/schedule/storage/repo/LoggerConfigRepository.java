package com.spc.schedule.storage.repo;

import com.spc.schedule.storage.bean.LoggerConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoggerConfigRepository extends JpaRepository<LoggerConfig, Integer> {
}
