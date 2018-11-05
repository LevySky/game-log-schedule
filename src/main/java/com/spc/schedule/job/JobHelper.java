package com.spc.schedule.job;

import com.spc.common.CountProxyHelper;
import com.spc.schedule.storage.bean.HandleResult;
import com.spc.schedule.storage.service.JobInfoManager;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

public class JobHelper {

    public static void count(String className,List<Integer> serverIdList){
        Date start = new Date();
        try{
            System.out.println("开始"+className+"Job："+start);
            HandleResult hr = CountProxyHelper.getProxyMap(className).etl(new Date(),serverIdList);//统计当天数据
            System.out.println("结束"+className+"Job："+new Date());
            JobInfoManager.record(start,className,hr.isOk()?1:2,hr.getResone(),null);
        }catch (Exception e){
            JobInfoManager.record(start,className,2,null,e);
            e.printStackTrace();
        }


    }
}
