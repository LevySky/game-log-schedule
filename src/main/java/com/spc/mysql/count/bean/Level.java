package com.spc.mysql.count.bean;
import com.alibaba.fastjson.JSONObject;
import javax.persistence.Id;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "t_c_level")
public class Level extends BaseBean implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer minLevel;
    private Integer maxLevel;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMinLevel() {
        return minLevel;
    }

    public void setMinLevel(Integer minLevel) {
        this.minLevel = minLevel;
    }

    public Integer getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(Integer maxLevel) {
        this.maxLevel = maxLevel;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
