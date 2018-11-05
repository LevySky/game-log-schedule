package com.spc.mysql.count.repository;

import com.alibaba.fastjson.JSONObject;
import com.spc.mysql.count.bean.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface LoginRepository extends JpaRepository<Login, Integer>,BaseRepository {
    @Query(value = "delete from t_c_login where server_id = ?1 and log_date = ?2",nativeQuery = true)
    @Modifying
    int deleteByDate(Integer serverId,String dataStr);


    @Query(value = "select * from t_c_login where log_date BETWEEN ?1 and ?2",nativeQuery = true)
    List<Login> findByDate(String startDateStr,String endDatetr);

    @Query(value = "select count(DISTINCT player_id) as loginTimes,log_date as logDate from t_c_login where server_id = ?1 and log_date = ?2 GROUP BY log_date",nativeQuery = true)
    JSONObject groupByDate(Integer serverId, String dataStr);

}
