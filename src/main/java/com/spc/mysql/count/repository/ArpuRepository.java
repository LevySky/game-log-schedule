package com.spc.mysql.count.repository;

import com.spc.mysql.count.bean.Arpu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface ArpuRepository extends JpaRepository<Arpu, Integer> {
    @Query(value = "delete from t_c_arpu where log_date = ?1",nativeQuery = true)
    @Modifying
    int deleteByDate(String dataStr);
}
