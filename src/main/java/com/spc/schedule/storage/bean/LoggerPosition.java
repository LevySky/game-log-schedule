package com.spc.schedule.storage.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "t_spc_logger_position")
public class LoggerPosition implements Serializable {

    private static final long serialVersionUID = 1L;

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    private Integer serverId;
    private Long position;
    private String fileName;
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getPosition() {
        return position == null ? 0l : position;
    }

    public void setPosition(Long postion) {
        this.position = postion;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }
}
