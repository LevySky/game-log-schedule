package com.spc.common;

import com.alibaba.fastjson.JSONObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JdbcManager<T> {



    public  List<T> query(String sql,Class<T> t){
        Connection conn = null;
        try {
            List<T> list = new ArrayList<>();
            conn =  DriverManager.getConnection("jdbc:mysql://192.168.1.123:3306/user","game","game");

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            ResultSetMetaData md = rs.getMetaData();
            int columnCount = md.getColumnCount();
            while (rs.next()) {
                Map<String,Object> rowData = new HashMap<String,Object>();
                for (int i = 1; i <= columnCount; i++) {
                    rowData.put(md.getColumnName(i), rs.getObject(i));
                }
                String jsonStr = JSONObject.toJSONString(rowData);
                T bean = JSONObject.parseObject(jsonStr,t);
                list.add(bean);
            }
            //System.out.println(JSONObject.toJSONString(pojoList.get(0)));
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }finally {
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }



}
