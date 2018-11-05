package com.spc.mysql.db.dynamic;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class JdbcTemplateDataSourceConfig {


    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "spring.datasource.user")
    public DynamicDataSource dataSource(@Qualifier("userDataSource") DataSource userDataSource,@Qualifier("mainDataSource") DataSource mainDataSource) {
        System.out.println("-------------------- dataSource init ---------------------");

        Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
        targetDataSources.put(DataBaseType.main_db, mainDataSource);
        targetDataSources.put(DataBaseType.user_db, userDataSource);
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setTargetDataSources(targetDataSources);// 该方法是AbstractRoutingDataSource的方法
        dynamicDataSource.setDefaultTargetDataSource(mainDataSource);// 默认的datasource设置为myTestDbDataSource
        System.out.println("-------------------- dataSource init end---------------------");
        return dynamicDataSource;
    }

    @Bean(name = "jdbcTemplate")
    public JdbcTemplate primaryJdbcTemplate(
            @Qualifier("dataSource") DynamicDataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

}
