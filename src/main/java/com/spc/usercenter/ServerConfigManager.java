package com.spc.usercenter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ServerConfigManager {


    private static Map<Integer, ServerConfig> map = new HashMap<>();
    private static List<ServerConfig> list = new ArrayList<>();


    public static void load() {
        map.clear();
        list.clear();
        list = ServerConfigRepository.findAll();
        for (ServerConfig sc : list) {
            map.put(sc.getId(), sc);
        }
        System.out.println("初始化用户中心数据---服务器列表:" + list.size());
    }

    public static ServerConfig getServerConfig(Integer id) {
        return map.get(id);
    }

    public static List<ServerConfig> getList() {
        return list;
    }


    /**
     * 获取有日志记录的服务器
     *
     * @return
     */
    public static List<ServerConfig> getLogList() {
        List<ServerConfig> logList = new ArrayList<>();
        for (ServerConfig sc : list) {
            if (sc != null && StringUtils.isNotEmpty(sc.getLogFileDir())) {
                logList.add(sc);
            }
        }
        return logList;
    }


    /**
     * 获取有日志记录的服务器ID
     *
     * @return
     */
    public static List<Integer> getLogListID() {
        List<Integer> logList = new ArrayList<>();
        for (ServerConfig sc : list) {
            if (sc != null && StringUtils.isNotEmpty(sc.getLogFileDir())) {
                logList.add(sc.getId());
            }
        }
        return logList;
    }


}
