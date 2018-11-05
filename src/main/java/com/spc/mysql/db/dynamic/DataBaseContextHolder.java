package com.spc.mysql.db.dynamic;

/**
 * 切换数据源只要在程序中使用 DBContextHolder.setDBType(1) 即可完成数据源切换
 *
 * @author LEVY
 */


import com.spc.usercenter.ServerConfig;

/**
 * 作用：
 * 1、保存一个线程安全的DatabaseType容器
 */
public class DataBaseContextHolder {

    private static  ThreadLocal<Object> contextHolder = new ThreadLocal<Object>();

    private static  ThreadLocal<ServerConfig>  serverConfigHolder= new ThreadLocal<ServerConfig>();
    public static void setObject(Object type){
        contextHolder.set(type);
    }
    public static Object getObject(){
        return contextHolder.get();
    }

    public static void setServerConfig(ServerConfig config){
        serverConfigHolder.set(config);
    }

    public static ServerConfig getServerConfig(){
        return serverConfigHolder.get();
    }
}
