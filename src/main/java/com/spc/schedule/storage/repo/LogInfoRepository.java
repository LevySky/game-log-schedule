package com.spc.schedule.storage.repo;

import com.spc.schedule.storage.bean.LogInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogInfoRepository extends JpaRepository<LogInfo, Integer> {

}
