package com.spc.mysql.count.bean;

import com.alibaba.fastjson.JSONObject;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "t_c_items")
public class Items extends BaseBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer opType;
    @Column(name = "`change`",length = 20)
    private Long change;
    private Integer playerNum;
    private Integer updateType;
    private Integer tmplateId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOpType() {
        return opType;
    }

    public void setOpType(Integer opType) {
        this.opType = opType;
    }

    public Long getChange() {
        return change;
    }

    public void setChange(Long change) {
        this.change = change;
    }

    public Integer getPlayerNum() {
        return playerNum;
    }

    public void setPlayerNum(Integer playerNum) {
        this.playerNum = playerNum;
    }

    public Integer getUpdateType() {
        return updateType;
    }

    public void setUpdateType(Integer updateType) {
        this.updateType = updateType;
    }

    public Integer getTmplateId() {
        return tmplateId;
    }

    public void setTmplateId(Integer tmplateId) {
        this.tmplateId = tmplateId;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
