package com.spc.mysql.count.repository;

import com.spc.mysql.count.bean.Online;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface OnlineRepository extends JpaRepository<Online, Integer> {
    @Query(value = "delete from t_c_online where server_id = ?1 and log_date  BETWEEN ?2  and ?3",nativeQuery = true)
    @Modifying
    int deleteByDates(Integer serverId,String startStr,String dataStr);
}
