package com.spc.schedule.storage.bean;

import org.springframework.data.annotation.Transient;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "t_spc_job_config")
public class JobConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name = "`group`")
    private String group;
    private String jobName;
    private String className;
    private String corn;
    private String info;

    @Transient
    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
