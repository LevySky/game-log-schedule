package com.spc.mysql.count.repository;

import com.spc.mysql.count.bean.Items;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface ItemsRepository extends JpaRepository<Items, Integer>,BaseRepository {
    @Query(value = "delete from t_c_items where server_id = ?1 and log_date = ?2",nativeQuery = true)
    @Modifying
    int deleteByDate(Integer serverId,String dataStr);
}
