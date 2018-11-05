package com.spc.mysql.log;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class StatementtHelper {


    private static ConcurrentHashMap<String,String> tableStructMap = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String,String> insertSqlMap = new ConcurrentHashMap<>();

    private static  synchronized void putMap(String tableName,String insertSql){
        tableStructMap.put(tableName,insertSql);
    }


    public static boolean isSame(String tableName,String insertSql){
        String exist = tableStructMap.get(tableName);
        if(exist == null){
            putMap(tableName,insertSql);
            return true;
        }else {
            return exist.equals(insertSql);
        }
    }


    public static boolean isExist(JdbcTemplate jt,String tableName,Integer serverId){


        String key = serverId+"__"+tableName;
        if(insertSqlMap.get(key) != null){
            return true;
        }

        tableName = "'t_log_"+tableName+"';";
        String sql = "select COUNT(1) as count from INFORMATION_SCHEMA.TABLES where  TABLE_NAME="+tableName;
        List<Boolean> list = jt.query(sql, new RowMapper<Boolean>() {
            @Override
            public Boolean mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getInt("count") > 0;
            }
        });
        if(list != null && !list.isEmpty() && list.get(0)){
            insertSqlMap.put(key,sql);
            return true;
        }
        LoggerFactory.getLogger("insert").info("数据库创建！！！");
        return false;
    }


    public static String getCreateSql(String dbName, String recordStr, String logSpliteType) {
        if (StringUtils.isEmpty(recordStr)) {
            return null;
        }
        String[] splited = recordStr.split(logSpliteType);
        Map<String, Object> map = JSONObject.parseObject("{" + splited[1]);
        StringBuffer sb = new StringBuffer();
        sb.append("create table `t_log_").append(dbName).append("`(");
        for (String key : map.keySet()) {
            sb.append("`" + key + "`").append(getDBFieldType(key, "" + map.get(key))).append(",");
        }
        sb.append("`logDate` datetime DEFAULT NULL,`writeTime` datetime DEFAULT NULL)");
        sb.append("ENGINE=MyISAM DEFAULT CHARSET=utf8;");
        return sb.toString();
    }


    public static String getInsertSql(String dbName, String recordStr, String logSpliteType) {
        if (StringUtils.isEmpty(recordStr)) {
            return null;
        }
        String[] splited = recordStr.split(logSpliteType);
        Map<String, Object> map = JSONObject.parseObject("{" + splited[1]);
        StringBuffer sb = new StringBuffer();
        sb.append("insert into t_log_").append(dbName).append("(");
        for (String key : map.keySet()) {
            sb.append("`" + key + "`").append(",");
        }
        sb.append("`logDate`,`writeTime`");
        sb.append(")").append(" values(");
        for (String key : map.keySet()) {
            sb.append("?").append(",");
        }
        sb.append("?,?)");
//        System.out.println(sb.toString());
        return sb.toString();
    }

    public static void setPs(PreparedStatement ps, String recordStr, String logSpliteType) {

        try {
            if (StringUtils.isEmpty(recordStr)) {
                return;
            }
            String[] splited = recordStr.split(logSpliteType);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Map<String, Object> map = JSONObject.parseObject("{" + splited[1]);
            String rexp = "^.*time$";
            Pattern pattern = Pattern.compile(rexp, Pattern.CASE_INSENSITIVE);
            int i = 1;
            for (String key : map.keySet()) {


                Object value = map.get(key);
                if (pattern.matcher(key).find() && value.toString().length() > 11) {
                    //System.out.println(key+value);
                    ps.setObject(i, new Date((long) value));
                } else if ("playerId".equals(key)) {
                    ps.setString(i, value.toString());
                } else {
                    ps.setObject(i, value.toString());
                }
                i++;
            }
            ps.setObject(map.size() + 1, sdf.parse(splited[0].split(",")[0]));
            ps.setObject(map.size() + 2, new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String getDBFieldType(String key, String value) {
        String type = "varchar(255) DEFAULT NULL";
        switch (key) {
            case "time":
            case "logoutTime":
            case "loginTime":
                return "datetime DEFAULT NULL";
            case "playerId":
            case "ids":
                return type;
        }
        if (Pattern.matches("[0-9]+", value)) {
            return "int(11) DEFAULT NULL";
        }
        return type;
    }
}
