package com.spc.common;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class CountProxyHelper {

    public static final String CLASSPATH = "com.spc.mysql.count.mgr.";

    private static ConcurrentHashMap<String, BaseCountManager> proxyMap  = new ConcurrentHashMap<>();

    public static BaseCountManager getProxyMap(String type) {
        return proxyMap.get(type);
    }

    public static List<String> getAllCountMgrNames() {
        List<String> list = new ArrayList<>();
        proxyMap.keySet().forEach(key -> list.add(key));
        return list;
    }


    public  static BaseCountManager  load(BaseCountManager baseCountManager){

        String name = baseCountManager.getClass().getSimpleName();
//        if(name.indexOf("Mgr") > 0){
//            name = "SQL_" +name;
//        }else{
//            name = "M_" +name;
//        }
        System.out.println(name);
        return proxyMap.putIfAbsent(name,baseCountManager);
    }
}
