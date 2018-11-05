package com.spc.schedule.job;

import com.spc.schedule.storage.bean.LoggerPosition;
import com.spc.schedule.storage.bean.LoggerPositionHistory;
import com.spc.schedule.storage.repo.LoggerPositionHistoryRepository;
import com.spc.schedule.storage.repo.LoggerPositionRepository;
import com.spc.schedule.storage.service.JobInfoManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

@Component
public class LoggerResetJob implements Job {

    public static LoggerResetJob loggerJob;

    @PostConstruct
    public void init(){
        loggerJob = this;
    }

    @Autowired
    LoggerPositionRepository posRepository;
    @Autowired
    LoggerPositionHistoryRepository posHisRepository;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {


        Date start = new Date();

        try {
            System.out.println("开始重置日志日常任务：" + start);
            saveHistory();
            loggerJob.posRepository.deleteAll();
            System.out.println("结束重置日志日常任务：" + new Date());
            JobInfoManager.record(start,this.getClass().getName(),1,null,null);
        } catch (Exception e) {
            e.printStackTrace();
            JobInfoManager.record(start,this.getClass().getName(),2,null,e);
        }

    }

   public void saveHistory(){
       List<LoggerPosition> lpList = loggerJob.posRepository.findAll();
       lpList.forEach(lp->{
           LoggerPositionHistory lph = new LoggerPositionHistory();
           lph.convert2History(lp);
           loggerJob.posHisRepository.save(lph);
       });
   }

}

