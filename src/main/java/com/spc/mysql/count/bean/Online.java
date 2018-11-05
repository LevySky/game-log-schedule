package com.spc.mysql.count.bean;

import com.alibaba.fastjson.JSONObject;

import javax.persistence.*;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;


@Entity
@Table(name = "t_c_online")
public class Online extends BaseBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Long onlineNum;

    @Transient
    Date loginTime;
    Date logoutTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getOnlineNum() {
        return onlineNum;
    }

    public void setOnlineNum(Long onlineNum) {
        this.onlineNum = onlineNum;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public Date getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(Date logoutTime) {
        this.logoutTime = logoutTime;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
