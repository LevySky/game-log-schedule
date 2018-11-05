package com.spc.schedule.storage.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "t_spc_log_count_info")
public class LogCountInfo implements Serializable{

    private static final long serialVersionUID = 1L;

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    private String servers;
    private String execClass;
    private Date startTime;
    private Date endTime;
    @Column(name = "`desc`")
    private String desc;




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

    public String getExecClass() {
        return execClass;
    }

    public void setExecClass(String execClass) {
        this.execClass = execClass;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
