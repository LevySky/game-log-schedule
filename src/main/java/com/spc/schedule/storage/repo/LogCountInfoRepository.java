package com.spc.schedule.storage.repo;

import com.spc.schedule.storage.bean.LogCountInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogCountInfoRepository extends JpaRepository<LogCountInfo, Integer> {

}
