package com.spc.schedule.storage.service;

import com.spc.schedule.storage.bean.LoggerConfig;
import com.spc.schedule.storage.repo.LoggerConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class LoggerConfigManager {

    @Autowired
    LoggerConfigRepository repository;

    public static LoggerConfigManager loggerConfigManager;

    @PostConstruct
    public void init() {
     loggerConfigManager = this;
    }

    private static List<LoggerConfig> list = new ArrayList<>();
    private static Map<String,List<LoggerConfig>> dataMap = new HashMap<>();

    public static void load(){

        dataMap.clear();
        list = loggerConfigManager.repository.findAll();

        list.forEach(lc->{
            List<LoggerConfig> tmpList = dataMap.get(lc.getLogFileName());
            if(tmpList == null){
                tmpList = new ArrayList<>();
            }

            boolean exist = false;
            for(LoggerConfig tmp : tmpList){
                if(tmp.getCollectionName().equals(lc.getCollectionName())){
                    exist = true;
                    break;
                }
            }
            if(!exist){
                tmpList.add(lc);
            }
            dataMap.put(lc.getLogFileName(),tmpList);
        });

        System.out.println("加载日志配置类！！！ "+list.size());

    }

    public static  List<LoggerConfig> getListByFile(String fileName){
             return dataMap.get(fileName);
    }

    public static LoggerConfig getByCollName(String collName){
        return list.stream().filter(item-> collName.equals(item.getCollectionName())).findAny().get();
    }

    public static List<LoggerConfig> getList(){
        return list;
    }


    public static Set<String> getFileNames(){
        return dataMap.keySet();
    }



}
