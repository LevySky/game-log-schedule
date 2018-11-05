package com.spc.schedule.storage.repo;

import com.spc.schedule.storage.bean.LoggerPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoggerPositionRepository extends JpaRepository<LoggerPosition, Integer> {
    LoggerPosition findByServerIdAndFileName(Integer serverId,String fileName);
}
