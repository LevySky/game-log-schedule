package com.spc.mysql.count.bean;

import com.alibaba.fastjson.JSONObject;
import javax.persistence.Id;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.Serializable;

@Entity(name="t_c_task")
public class Task extends BaseBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer taskId;
    private Integer taskType;
    private Integer playerNum;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getTaskType() {
        return taskType;
    }

    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }

    public Integer getPlayerNum() {
        return playerNum;
    }

    public void setPlayerNum(Integer playerNum) {
        this.playerNum = playerNum;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
