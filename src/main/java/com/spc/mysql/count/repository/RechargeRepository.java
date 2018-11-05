package com.spc.mysql.count.repository;

import com.alibaba.fastjson.JSONObject;
import com.spc.mysql.count.bean.Recharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.ResultSet;
import java.util.List;

@Transactional
@Repository
public interface RechargeRepository extends JpaRepository<Recharge, Integer>,BaseRepository {
    @Query(value = "delete from t_c_recharge where server_id = ?1 and log_date = ?2",nativeQuery = true)
    @Modifying
    int deleteByDate(Integer serverId,String dataStr);

    @Query(value = "select cost ,count(DISTINCT player_id) as ignot,log_date as logDate from t_c_recharge where server_id = ?1 and log_date = ?2 GROUP BY log_date",nativeQuery = true)
    JSONObject findByDate(Integer serverId, String dataStr);
}
