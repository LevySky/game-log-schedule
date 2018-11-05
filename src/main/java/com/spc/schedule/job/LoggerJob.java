package com.spc.schedule.job;

import com.spc.common.LoggerManager;
import com.spc.schedule.ThreadHelper;
import com.spc.schedule.storage.bean.HandleResult;
import com.spc.schedule.storage.service.JobInfoManager;
import com.spc.schedule.storage.repo.LogInfoRepository;
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
 * 定时读取当天的数据
 */
@Component
public class LoggerJob implements Job {

    public LoggerJob loggerJob;
    @Autowired
    LogInfoRepository logInfoRepository;

    @PostConstruct
    public void init() {
        loggerJob = this;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        ServerConfigManager.load();//保证日志路径最新
        List<ServerConfig> scList = ServerConfigManager.getLogList();
        Set<String> fileNames = LoggerConfigManager.getFileNames();
        Date start = new Date();
        System.out.println("开始LoggerJob：" + start);

        for (ServerConfig sc : scList) {
            ThreadHelper.getThreadPollProxy().execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName()+"   "+sc.getId());
                    for (String fileName : fileNames) {
                        handleFile(start, sc.getId(), sc.getLogFileDir(), fileName);//http://192.168.1.123:8089/gamelogs/
                    }
                }
            });

        }

//        ThreadHelper.getThreadPollProxy().stop();
        System.out.println("结束LoggerJob：" + new Date());

    }

    /**
     * 文件处理
     *
     * @param start
     * @param serverId
     * @param dir
     * @param fileName
     */
    public void handleFile(Date start, Integer serverId, String dir, String fileName) {
        String desc = serverId + "--" + dir + fileName;
        try {
            HandleResult hr = LoggerManager.autoReadFile(serverId, dir, fileName);
            JobInfoManager.record(start, this.getClass().getName(), hr.isOk() ? 1 : 2, hr.getResone(), null);
        } catch (Exception e) {
            JobInfoManager.record(start, this.getClass().getName(), 2, desc, e);
            e.printStackTrace();
        }
    }


}

