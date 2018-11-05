package com.spc.mysql.count.bean;

import com.alibaba.fastjson.JSONObject;

import javax.persistence.*;

import java.io.Serializable;

@Entity(name="t_c_remain")
public class Remain extends BaseBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "`id`",length = 20)
    private Long id;
    private Integer registerNum;
    private Integer remain2;
    private Integer remain3;
    private Integer remain4;
    private Integer remain5;
    private Integer remain6;
    private Integer remain7;
    private Integer remain14;
    private Integer remain30;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRegisterNum() {
        return registerNum;
    }

    public void setRegisterNum(Integer registerNum) {
        this.registerNum = registerNum;
    }

    public Integer getRemain2() {
        return remain2;
    }

    public void setRemain2(Integer remain2) {
        this.remain2 = remain2;
    }

    public Integer getRemain3() {
        return remain3;
    }

    public void setRemain3(Integer remain3) {
        this.remain3 = remain3;
    }

    public Integer getRemain4() {
        return remain4;
    }

    public void setRemain4(Integer remain4) {
        this.remain4 = remain4;
    }

    public Integer getRemain5() {
        return remain5;
    }

    public void setRemain5(Integer remain5) {
        this.remain5 = remain5;
    }

    public Integer getRemain6() {
        return remain6;
    }

    public void setRemain6(Integer remain6) {
        this.remain6 = remain6;
    }

    public Integer getRemain7() {
        return remain7;
    }

    public void setRemain7(Integer remain7) {
        this.remain7 = remain7;
    }

    public Integer getRemain14() {
        return remain14;
    }

    public void setRemain14(Integer remain14) {
        this.remain14 = remain14;
    }

    public Integer getRemain30() {
        return remain30;
    }

    public void setRemain30(Integer remain30) {
        this.remain30 = remain30;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
