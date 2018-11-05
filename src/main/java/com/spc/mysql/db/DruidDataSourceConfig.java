package com.spc.mysql.db;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Configuration
public class DruidDataSourceConfig {

    @Autowired
    private Environment env;

    @Primary
    @Bean(name = "mainDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.main")
    public DataSource mainDataSource() {
        System.out.println("-------------------- mainDataSource init ---------------------");
//        MyConfig mc = new MyConfig();
//        mc.init(env);

        return new DruidDataSource();
    }

    @Bean(name = "userDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.user")
    public DataSource userDataSource() {
        System.out.println("-------------------- userDataSource init ---------------------");
        return new DruidDataSource();
    }

    //@Primary
//    @Bean(name = "dataSource")
//    @ConfigurationProperties(prefix = "spring.datasource.user")
//    public DynamicDataSource dataSource(@Qualifier("mainDataSource") DataSource mainDataSource, @Qualifier("userDataSource")
//            DataSource userDataSource) {
//        System.out.println("-------------------- dataSource init ---------------------");
//
//        Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
//        targetDataSources.put(DataBaseType.main_db, mainDataSource);
//        targetDataSources.put(DataBaseType.user_db, userDataSource);
//        DynamicDataSource dynamicDataSource = new DynamicDataSource();
//        dynamicDataSource.setTargetDataSources(targetDataSources);// 该方法是AbstractRoutingDataSource的方法
//        dynamicDataSource.setDefaultTargetDataSource(mainDataSource());// 默认的datasource设置为myTestDbDataSource
//        System.out.println("-------------------- dataSource init end---------------------");
//        return dynamicDataSource;
//    }


}
