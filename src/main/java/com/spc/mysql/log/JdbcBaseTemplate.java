package com.spc.mysql.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Service
public class JdbcBaseTemplate {


    private final int BATCH_NUM = 1000;

    @Autowired
    JdbcTemplate jdbcTemplate;


    public boolean createStruct(Integer serverId,String sql) {
        try {
            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean modifyStruct(String sql) {
        jdbcTemplate.execute(sql);
        return true;
    }


    /**
     * 入库
     * @param serverId
     * @param dbName
     * @param recordList
     * @param logSpliteType
     * @return
     */
    public boolean insert(Integer serverId,String dbName, List<String> recordList, String logSpliteType) {

        if(!StatementtHelper.isExist(jdbcTemplate,dbName,serverId)){
            createStruct(serverId,StatementtHelper.getCreateSql(dbName, recordList.get(0), logSpliteType));
        }


        String sql = StatementtHelper.getInsertSql(dbName, recordList.get(0), logSpliteType);

        //:TODO 自动修改表结构
//        if(!StatementtHelper.isSame(dbName,sql)){
//            modifyStruct(sql);
//        }



        int batchNum = (int)Math.ceil(recordList.size() / (BATCH_NUM * 1.0));

        for (int n = 0; n < batchNum; n++) {

            int startPos = n*BATCH_NUM;
            int endPos = n*BATCH_NUM+BATCH_NUM;
            endPos = endPos > recordList.size() ? recordList.size() : endPos;

            List<String> list = recordList.subList(startPos,endPos);
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    String recordStr = list.get(i);
                    try {
                        StatementtHelper.setPs(ps, recordStr, logSpliteType);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public int getBatchSize() {
                    return list.size();
                }
            });
        }
        return true;
    }

    public <T> List<T> findAll(Integer serverId,Class<T> e , String sql) {
        RowMapper<T> rowMapper = new BeanPropertyRowMapper<>(e);
        return jdbcTemplate.query(sql,rowMapper);
    }

}
