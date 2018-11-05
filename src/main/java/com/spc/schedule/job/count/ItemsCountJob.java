package com.spc.schedule.job.count;

import com.spc.schedule.job.JobHelper;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

/**
 * 实现Job接口
 * @author yvan
 *
 */
@Component
public class ItemsCountJob implements Job{


    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {

        JobHelper.count("ItemsCountMgr", null);

    }




}
