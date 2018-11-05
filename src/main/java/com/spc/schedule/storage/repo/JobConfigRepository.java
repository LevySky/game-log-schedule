package com.spc.schedule.storage.repo;

import com.spc.schedule.storage.bean.JobConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobConfigRepository extends JpaRepository<JobConfig, Integer> {
     JobConfig findByClassName(String className);
     JobConfig findByJobNameAndAndGroup(String jobName,String group);
}
