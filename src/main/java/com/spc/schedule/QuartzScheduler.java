package com.spc.schedule;

import com.spc.schedule.storage.bean.JobConfig;
import com.spc.schedule.storage.repo.JobConfigRepository;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.Map;


/**
 * 任务调度处理
 * @author yvan
 *
 */
@Configuration
public class QuartzScheduler {
    // 任务调度
    @Autowired
    public Scheduler scheduler;
    @Autowired
    JobConfigRepository jobConfigRepository;

    /**
     * 开始执行所有任务
     *
     * @throws SchedulerException
     */
    public void startAllJob() throws SchedulerException {
        scheduler.start();
    }


    public void addJob(Map map) throws Exception{

        JobConfig job = new JobConfig();
        job.setGroup(map.get("group").toString());
        job.setJobName(map.get("jobName").toString());
        job.setCorn(map.get("corn").toString());
        job.setInfo(""+map.get("info"));
        job.setClassName(map.get("className").toString());

        JobConfig exist = jobConfigRepository.findByClassName(job.getClassName());
        if(exist != null){
             throw new Exception("一个任务类只能由一个定时器调度！ 可以对存在的定时任务进行时间修改或删除在添加");
        }

        if(initJob(job)){
            jobConfigRepository.save(job);
        }else{
            throw new Exception("添加失败！！！");
        }

    }

    /**
     *
     * @param map
     * @throws Exception
     */
    public void editJob(Map map) throws Exception{

        JobConfig exist = jobConfigRepository.findByClassName(map.get("className").toString());

        if(modifyJob(exist,map)){
            exist.setCorn(map.get("corn").toString());
            exist.setInfo(map.get("info").toString());
            jobConfigRepository.save(exist);
        }else{
            throw new Exception("修改失败！！！");
        }

    }

    /**
     * 获取Job信息
     *
     * @param name
     * @param group
     * @return
     * @throws SchedulerException
     */
    public String getJobInfo(String name, String group) throws SchedulerException {
        TriggerKey triggerKey = new TriggerKey(name, group);
        CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        return String.format("time:%s,state:%s", cronTrigger.getCronExpression(),
                scheduler.getTriggerState(triggerKey).name());
    }

    /**
     *
     * @param exist
     * @param map
     * @return
     * @throws SchedulerException
     */
    public boolean modifyJob(JobConfig exist,Map map) throws SchedulerException {
        Date date = null;
        TriggerKey triggerKey = new TriggerKey(exist.getJobName(), exist.getGroup());
        CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        String oldTime = cronTrigger.getCronExpression();
        String newTime = map.get("corn").toString();
        if (!oldTime.equalsIgnoreCase(newTime) && StringUtils.isNotEmpty(newTime)) {
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(newTime);
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(map.get("jobName").toString(), map.get("group").toString())
                    .withSchedule(cronScheduleBuilder).build();
            date = scheduler.rescheduleJob(triggerKey, trigger);
        }
        return date != null;
    }

    /**
     * 暂停所有任务
     *
     * @throws SchedulerException
     */
    public void pauseAllJob() throws SchedulerException {
        scheduler.pauseAll();
    }

    /**
     * 暂停某个任务
     *
     * @param name
     * @param group
     * @throws SchedulerException
     */
    public void pauseJob(String name, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null)
            return;
        scheduler.pauseJob(jobKey);
    }

    /**
     * 恢复所有任务
     *
     * @throws SchedulerException
     */
    public void resumeAllJob() throws SchedulerException {
        scheduler.resumeAll();
    }

    /**
     * 恢复某个任务
     *
     * @param name
     * @param group
     * @throws SchedulerException
     */
    public void resumeJob(String name, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null)
            return;
        scheduler.resumeJob(jobKey);
    }

    /**
     * 删除某个任务
     *
     * @param name
     * @param group
     * @throws SchedulerException
     */
    public void deleteJob(String name, String group) throws SchedulerException {
        try{
            JobKey jobKey = new JobKey(name, group);
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            if (jobDetail == null)
                return;
            scheduler.deleteJob(jobKey);
            jobConfigRepository.delete(jobConfigRepository.findByJobNameAndAndGroup(name,group));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public boolean initJob(JobConfig config){
        // 通过JobBuilder构建JobDetail实例，JobDetail规定只能是实现Job接口的实例
        // JobDetail 是具体Job实例
        try {
            Object obj = Class.forName(config.getClassName()).newInstance();
            JobDetail jobDetail = JobBuilder.newJob((Class<? extends Job>) obj.getClass()).withIdentity(config.getJobName(), config.getGroup()).build();
            // 基于表达式构建触发器
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(config.getCorn());//"0/5 * * * * ?"
            // CronTrigger表达式触发器 继承于Trigger
            // TriggerBuilder 用于构建触发器实例
            CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(config.getJobName(), config.getGroup())
                    .withSchedule(cronScheduleBuilder).build();
            scheduler.scheduleJob(jobDetail,cronTrigger);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}