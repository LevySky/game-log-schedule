package com.spc.common;

import com.spc.schedule.storage.bean.HandleResult;
import com.spc.schedule.storage.bean.LogCountInfo;
import com.spc.schedule.storage.service.JobInfoManager;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class LoggerCountRecoverManager {


    /**
     * 日志恢复
     * @param logInfo
     */
    public static void  execute(LogCountInfo logInfo){





        List<Integer> serverList = getIntServerList(logInfo.getServers());

        List<Date> dates = Constant.findDatesAddOne(logInfo.getStartTime(),logInfo.getEndTime());
        List<String> execs = getExecClassList(logInfo.getExecClass());


        if(dates == null || execs == null || execs.isEmpty()){
            return;
        }

        for(Date date : dates){
            execs.forEach(exec->{
                Date start = new Date();
                try {
                    CountManagerProxy proxy = new CountManagerProxy(exec);
                    long delCount = proxy.delete(date,serverList);;//删除当天数据
                    HandleResult hr = proxy.etl(date,serverList);;//统计当天数据
                    JobInfoManager.record(start,exec,hr.isOk()?1:2,hr.getResone()+" 删除: "+delCount,null);
                } catch (Exception e) {
                    e.printStackTrace();
                    JobInfoManager.record(start,exec,2,null,e);
                }
            });



        }

    }

    public static List<Integer> getIntServerList(String servers){
        List<Integer> serverList = null;
        if(StringUtils.isNotEmpty(servers)){

            if(Constant.LOGGER_RECOVER_LOGGERFILE_ALL.equals(servers)){
                return serverList;
            }

            serverList = new ArrayList<>();
            String[] ids =  servers.trim().split(",");
            for(String id : ids){
                serverList.add(Integer.parseInt(id));
            }
        }
        return serverList;
    }

    public static List<String> getExecClassList(String clazzs) {
        List<String> serverList = null;
        try {
            if(StringUtils.isNotEmpty(clazzs)){
                if(Constant.LOGGER_RECOVER_LOGGERFILE_ALL.equals(clazzs)){
                    return  CountProxyHelper.getAllCountMgrNames();
                }
                serverList = new ArrayList<>();
                String[] cs =  clazzs.trim().split(",");
                for(String c : cs){
                    serverList.add(c);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serverList;
    }



}
