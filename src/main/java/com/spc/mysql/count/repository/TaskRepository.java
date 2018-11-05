package com.spc.mysql.count.repository;

import com.spc.mysql.count.bean.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface TaskRepository extends JpaRepository<Task,Integer>,BaseRepository {
    @Query(value = "delete from t_c_task where server_id = ?1 and log_date = ?2",nativeQuery = true)
    @Modifying
    int deleteByDate(Integer serverId,String dataStr);
  
}
