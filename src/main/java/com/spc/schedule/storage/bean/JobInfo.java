package com.spc.schedule.storage.bean;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "t_spc_job_info")
public class JobInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    private Integer jobConfigId;
    private Date startTime;
    private Date endTime;
    private Integer status;
    @Column(length = 1024)
    private String info;


    @Transient
    private String group;
    @Transient
    private String jobName;
    @Transient
    private String className;
    @Transient
    private String corn;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Integer getJobConfigId() {
        return jobConfigId;
    }

    public void setJobConfigId(Integer jobConfigId) {
        this.jobConfigId = jobConfigId;
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

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCorn() {
        return corn;
    }

    public void setCorn(String corn) {
        this.corn = corn;
    }

}
