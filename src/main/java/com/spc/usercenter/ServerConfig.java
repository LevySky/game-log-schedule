package com.spc.usercenter;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;


public class ServerConfig implements Serializable{

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String serverName;


    private String maxLoad;


    private Boolean isSuggest;

    private Integer status;


    private String apiUrl;


    private String dbUrl;


    private String logFileDir;


    private String logDBUrl;

    private String logDBPwd;
    private String logDBUserName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getMaxLoad() {
        return maxLoad;
    }

    public void setMaxLoad(String maxLoad) {
        this.maxLoad = maxLoad;
    }

    public Boolean getIsSuggest() {
        return isSuggest;
    }

    public void setIsSuggest(Boolean isSuggest) {
        this.isSuggest = isSuggest;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }



    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }


    public String getLogFileDir() {
        return logFileDir;
    }

    public void setLogFileDir(String logFileDir) {
        this.logFileDir = logFileDir;
    }

    public String getLogDBUrl() {
        return logDBUrl;
    }

    public void setLogDBUrl(String logDBUrl) {
        this.logDBUrl = logDBUrl;
    }

    public String getLogDBPwd() {
        return logDBPwd;
    }

    public void setLogDBPwd(String logDBPwd) {
        this.logDBPwd = logDBPwd;
    }

    public String getLogDBUserName() {
        return logDBUserName;
    }

    public void setLogDBUserName(String logDBUserName) {
        this.logDBUserName = logDBUserName;
    }

    @Override
    public String toString(){
        return JSONObject.toJSONString(this);
    }
}
