package com.spc.mysql.count;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JdbcBaseTemplate4Count<T> {

    @Autowired
    static JdbcTemplate jdbcTemplate;


    public List<T> findAll(Integer serverId,Class<T> e , String sql) {
        RowMapper<T> rowMapper = new BeanPropertyRowMapper<>(e);
        return jdbcTemplate.query(sql,rowMapper);
    }

}
