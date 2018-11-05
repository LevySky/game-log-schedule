package com.spc.schedule.job;

import com.spc.common.Constant;
import com.spc.common.LoggerManager;
import com.spc.schedule.ThreadHelper;
import com.spc.schedule.storage.bean.HandleResult;
import com.spc.schedule.storage.repo.LogInfoRepository;
import com.spc.schedule.storage.service.JobInfoManager;
import com.spc.schedule.storage.service.LoggerConfigManager;
import com.spc.usercenter.ServerConfig;
import com.spc.usercenter.ServerConfigManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 读取前一天的的最后一次数据
 */
@Component
public class LoggerLastJob implements Job {

    public LoggerLastJob loggerJob;

    @PostConstruct
    public void init(){
        loggerJob = this;
    }

    @Autowired
    LogInfoRepository logInfoRepository;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        List<ServerConfig> scList = ServerConfigManager.getLogList();
        Set<String> fileNames = LoggerConfigManager.getFileNames();
        Date start = new Date();
        System.out.println("开始LoggerLastJob：" + start);



        for(ServerConfig sc : scList){
            ThreadHelper.getThreadPollProxy().execute(new Runnable() {
                @Override
                public void run() {
                    for(String fileName : fileNames){
                        handleFile(start,sc.getId(),sc.getLogFileDir(),fileName);//http://192.168.1.123:8089/gamelogs/
                    }
                }
            });
        }
        System.out.println("结束LoggerLastJob：" + new Date());

    }

    /**
     * 文件处理
     * @param start
     * @param serverId
     * @param dir
     * @param fileName
     */
    public void handleFile(Date start,Integer serverId,String dir,String fileName){

        String dateStr = Constant.getLastDayString(new Date());
        String desc = serverId+"--"+dir+fileName;
        try {
            long  skip = LoggerManager.getLastPos4Last(serverId,fileName);
            if(skip == 0L){
                JobInfoManager.record(start, this.getClass().getName(), 2,serverId+"  "+fileName+" 异常，最后一次为偏移量为 0 ！！！",null);
                return;
            }
            HandleResult hr = LoggerManager.manualReadFile(serverId,dir,fileName,dateStr,skip);
            desc += (dateStr==null?"":dateStr) + "   "+skip;
            JobInfoManager.record(start, this.getClass().getName(), hr.isOk()?1:2,hr.getResone(),null);
        } catch (Exception e) {
            JobInfoManager.record(start, this.getClass().getName(), 2, desc,e);
            e.printStackTrace();
        }
    }

}

