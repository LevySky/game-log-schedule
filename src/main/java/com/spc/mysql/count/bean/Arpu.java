package com.spc.mysql.count.bean;

import com.alibaba.fastjson.JSONObject;

import javax.persistence.*;

import java.io.Serializable;


@Entity
@Table(name = "t_c_arpu")
public class Arpu extends BaseBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer recharge;
    private Integer payNum;
    private Integer activeNum;
    private Integer registerNum;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRecharge() {
        return recharge;
    }

    public void setRecharge(Integer recharge) {
        this.recharge = recharge;
    }

    public Integer getPayNum() {
        return payNum;
    }

    public void setPayNum(Integer payNum) {
        this.payNum = payNum;
    }

    public Integer getActiveNum() {
        return activeNum;
    }

    public void setActiveNum(Integer activeNum) {
        this.activeNum = activeNum;
    }

    public Integer getRegisterNum() {
        return registerNum;
    }

    public void setRegisterNum(Integer registerNum) {
        this.registerNum = registerNum;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
