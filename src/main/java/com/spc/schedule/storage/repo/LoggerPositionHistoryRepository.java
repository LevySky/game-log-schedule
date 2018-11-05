package com.spc.schedule.storage.repo;

import com.spc.schedule.storage.bean.LoggerPositionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoggerPositionHistoryRepository extends JpaRepository<LoggerPositionHistory, Integer> {
    List<LoggerPositionHistory> findByServerIdAndFileName(Integer serverId, String fileName);
}
