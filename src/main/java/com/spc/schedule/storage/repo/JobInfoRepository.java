package com.spc.schedule.storage.repo;

import com.spc.schedule.storage.bean.JobInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobInfoRepository extends JpaRepository<JobInfo, Integer> {

}
