package com.spc.mysql.count.bean;

import com.alibaba.fastjson.JSONObject;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name="t_c_online_times")
public class OnlineTimes extends BaseBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Long onlineTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Long getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(Long onlineTime) {
        this.onlineTime = onlineTime;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
