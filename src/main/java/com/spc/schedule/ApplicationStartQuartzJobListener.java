package com.spc.schedule;

import com.spc.schedule.storage.bean.JobConfig;
import com.spc.schedule.storage.repo.JobConfigRepository;
import com.spc.schedule.storage.service.LoggerConfigManager;
import com.spc.usercenter.ServerConfigManager;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.List;

@Configuration
public class ApplicationStartQuartzJobListener implements ApplicationListener<ContextRefreshedEvent>{

    @Autowired
    private QuartzScheduler quartzScheduler;

    @Autowired
    JobConfigRepository jobConfigRepository;
    /**
     * 初始启动quartz
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            System.out.println("开始启动任务...");

            List<JobConfig> list = jobConfigRepository.findAll();
            for(JobConfig config : list){
                quartzScheduler.initJob(config);
            }
            quartzScheduler.startAllJob();
            System.out.println("任务已经启动...");

            LoggerConfigManager.load();
            ServerConfigManager.load();

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }




    /**
     * 初始注入scheduler
     * @return
     * @throws SchedulerException
     */
    @Bean
    public Scheduler scheduler() throws SchedulerException{
        SchedulerFactory schedulerFactoryBean = new StdSchedulerFactory();
        return schedulerFactoryBean.getScheduler();
    }

}