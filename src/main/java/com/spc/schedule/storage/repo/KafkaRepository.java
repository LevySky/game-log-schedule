package com.spc.schedule.storage.repo;

import com.spc.schedule.storage.bean.KafkaOffset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KafkaRepository  extends JpaRepository<KafkaOffset, Integer> {

}
