package com.spc.mysql.count.bean;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "t_c_login")
public class Login extends BaseBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer loginTimes;
    private String ip;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLoginTimes() {
        return loginTimes;
    }

    public void setLoginTimes(Integer loginTimes) {
        this.loginTimes = loginTimes;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        if (StringUtils.isNotEmpty(ip)) {
            String[] ip_arr = ip.split(":");
            if (ip_arr != null && ip_arr.length > 0) {
                this.ip = ip_arr[0].substring(1);
                return;
            }
        }
        this.ip = ip;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
