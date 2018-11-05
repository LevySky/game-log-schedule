package com.spc.schedule.storage.bean;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "t_spc_kafka")
public class KafkaOffset implements Serializable {

    private static final long serialVersionUID = 1L;

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name = "`offset`")
    private Integer offset;
    private Integer lastOffset;
    @Column(name = "`partition`")
    private Integer  partition;
    private Integer  batchSize;
    private String  topic;
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getPartition() {
        return partition;
    }

    public void setPartition(Integer partition) {
        this.partition = partition;
    }

    public Integer getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getLastOffset() {
        return lastOffset;
    }

    public void setLastOffset(Integer lastOffset) {
        this.lastOffset = lastOffset;
    }
}
