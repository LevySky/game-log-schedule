package com.spc.usercenter;

import com.spc.common.JdbcManager;

import java.util.List;

public class ServerConfigRepository {


    public static List<ServerConfig> findAll(){
        JdbcManager<ServerConfig> jdbc = new JdbcManager();
        String sql = "select * from t_s_server";
        return jdbc.query(sql,ServerConfig.class);
    }

}
