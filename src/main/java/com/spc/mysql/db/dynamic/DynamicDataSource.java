package com.spc.mysql.db.dynamic;

import com.alibaba.druid.pool.DruidDataSource;
import com.spc.usercenter.ServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 动态数据源切换
 *
 * @author levy
 */
public class DynamicDataSource extends AbstractRoutingDataSource {


    private static Map<Object, Object> myTargetDataSources = new HashMap<Object, Object>();

    private Logger log = LoggerFactory.getLogger(DynamicDataSource.class);

    public static Map<Object, Object> getMyTargetDataSources() {
        return myTargetDataSources;
    }

    public void setMyTargetDataSources(Map<Object, Object> myTargetDataSources) {
        this.myTargetDataSources = myTargetDataSources;
    }

    @Override
    protected Object determineCurrentLookupKey() {

        DataBaseType dbt = (DataBaseType) DataBaseContextHolder.getObject();
        if (DataBaseType.logger_db.equals(dbt)) {
            ServerConfig sc = DataBaseContextHolder.getServerConfig();
            String newDBName = "logger_" + sc.getId();
            try {
                if (null == getMyTargetDataSources().get(newDBName) && isConn(sc)) {
                    log.info("创建 dataSourceName:   " + newDBName + "   Size:   " + getMyTargetDataSources().size());
                    getMyTargetDataSources().put(newDBName, this.getDataSource(sc));
                    super.setTargetDataSources(getMyTargetDataSources());
                    super.afterPropertiesSet();
                }
                return newDBName;
            } catch (Exception e1) {
                log.error("切换 dataSourceName:   " + newDBName + " 出错");
            }finally {
                log.info("当前 dataSourceName:   " + newDBName + "   Size:   " + getMyTargetDataSources().size());
            }
        }

//        log.error("切换服务器数据源出错！！！");
        return dbt == null ? DataBaseType.main_db : dbt;
    }

    public DataSource getDataSource(ServerConfig sc) {
        DruidDataSource dds = new DruidDataSource();

        String driverClass = "com.mysql.jdbc.Driver";
        String url = sc.getLogDBUrl()
                + "?autoReconnect=false&useUnicode=true&characterEncoding=UTF-8&useServerPrepStmts=true&rewriteBatchedStatements=true&allowMultiQueries=true";
        String userName = sc.getLogDBUserName();
        String password = sc.getLogDBPwd();

        dds.setDriverClassName(driverClass);
        dds.setUrl(url);
        dds.setUsername(userName);
        dds.setPassword(password);
        dds.setMaxActive(20);
        dds.setInitialSize(1);
        dds.setMaxWait(60000);
        dds.setMinIdle(1);
        dds.setTimeBetweenEvictionRunsMillis(6000);
        dds.setMinEvictableIdleTimeMillis(300000);
        dds.setValidationQuery("SELECT * from table");
        dds.setTestOnBorrow(true);
        dds.setTestOnReturn(true);
        dds.setTestWhileIdle(true);
        dds.setRemoveAbandoned(true);
        dds.setRemoveAbandonedTimeout(180);
        //log.info("创建log数据源成功"+config.getLoggerHost());
        dds.setConnectionErrorRetryAttempts(1);
        return dds;
    }

    public boolean isConn(ServerConfig sc) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(sc.getLogDBUrl(), sc.getLogDBUserName(), sc.getLogDBPwd());
            if (conn == null || conn.isClosed()) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

}
