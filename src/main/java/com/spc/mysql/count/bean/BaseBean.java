package com.spc.mysql.count.bean;

import javax.persistence.MappedSuperclass;
import java.text.SimpleDateFormat;
import java.util.Date;

@MappedSuperclass
public class BaseBean {

    private String playerId;
    private Integer serverId;
    private Date logDate;

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }

    public void setOriginLogDate(Date logDate) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // sdf.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
        this.logDate = logDate;//sdf.parse(sdf.format(logDate));
    }

}
