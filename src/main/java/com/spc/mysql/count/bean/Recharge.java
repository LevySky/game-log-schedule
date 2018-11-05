package com.spc.mysql.count.bean;

import com.alibaba.fastjson.JSONObject;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name="t_c_recharge")
public class Recharge extends BaseBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer cost;
    private Integer ignot;
    private Integer extraIgnot;

    @Transient
    private String dateStr;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getIgnot() {
        return ignot;
    }

    public void setIgnot(Integer ignot) {
        this.ignot = ignot;
    }

    public Integer getExtraIgnot() {
        return extraIgnot;
    }

    public void setExtraIgnot(Integer extraIgnot) {
        this.extraIgnot = extraIgnot;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
