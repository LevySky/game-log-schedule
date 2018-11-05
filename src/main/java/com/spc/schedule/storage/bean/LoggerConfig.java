package com.spc.schedule.storage.bean;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "t_spc_logger_config")
public class LoggerConfig implements Serializable{

    private static final long serialVersionUID = 1L;

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    private String filterFlag;
    private String spliteFlag;
    private String collectionName;
    private String logFileName;
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFilterFlag() {
        return filterFlag;
    }

    public void setFilterFlag(String filterFlag) {
        this.filterFlag = filterFlag;
    }

    public String getSpliteFlag() {
        return spliteFlag;
    }

    public void setSpliteFlag(String spliteFlag) {
        this.spliteFlag = spliteFlag;
    }

    public String getCollectionName() {
        if(StringUtils.isNotEmpty(collectionName)){
            collectionName = collectionName.trim();
        }
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        if(StringUtils.isNotEmpty(collectionName)){
            collectionName = collectionName.trim();
        }
        this.collectionName = collectionName;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }


    public String getLogFileName() {
        if(StringUtils.isNotEmpty(logFileName)){
            logFileName = logFileName.trim();
        }
        return logFileName;
    }

    public void setLogFileName(String logFileName) {
        if(StringUtils.isNotEmpty(logFileName)){
            logFileName = logFileName.trim();
        }
        this.logFileName = logFileName;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
