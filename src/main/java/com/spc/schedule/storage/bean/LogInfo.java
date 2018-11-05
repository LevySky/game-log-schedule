package com.spc.schedule.storage.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "t_spc_log_info")
public class LogInfo implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    private String servers;
    private String logs;
    private Date startTime;
    private Date endTime;

    private Date runStartTime;
    private Date runEndTime;

    private Integer status;
    private String info;
    private Integer runTimes;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getServers() {
        return servers;
    }

    public void setServers(String servers) {
        this.servers = servers;
    }

    public String getLogs() {
        return logs;
    }

    public void setLogs(String logs) {
        this.logs = logs;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getRunStartTime() {
        return runStartTime;
    }

    public void setRunStartTime(Date runStartTime) {
        this.runStartTime = runStartTime;
    }

    public Date getRunEndTime() {
        return runEndTime;
    }

    public void setRunEndTime(Date runEndTime) {
        this.runEndTime = runEndTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Integer getRunTimes() {
        return runTimes;
    }

    public void setRunTimes(Integer runTimes) {
        this.runTimes = runTimes;
    }
}
