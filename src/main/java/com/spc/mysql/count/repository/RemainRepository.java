package com.spc.mysql.count.repository;

import com.alibaba.fastjson.JSONObject;
import com.spc.mysql.count.bean.Recharge;
import com.spc.mysql.count.bean.Remain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;

@Transactional
@Repository
public interface RemainRepository extends JpaRepository<Remain, Integer>,BaseRepository {
    @Query(value = "delete from t_c_remain where server_id = ?1 and log_date = ?2",nativeQuery = true)
    @Modifying
    int deleteByDate(Integer serverId,String dataStr);

    @Query(value = "delete from t_c_remain where log_date BETWEEN ?1 and ?2",nativeQuery = true)
    int deleteByDuration(String startDateStr,String endDateStr);

    Remain findByServerIdAndLogDate(Integer serverId, Date logDate);
}
