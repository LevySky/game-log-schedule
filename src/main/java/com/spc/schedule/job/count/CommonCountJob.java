package com.spc.schedule.job.count;

import com.spc.common.CountProxyHelper;
import com.spc.schedule.job.JobHelper;
import com.spc.usercenter.ServerConfigManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 实现Job接口
 * @author yvan
 *
 */
@Component
public class CommonCountJob implements Job{


    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        JobHelper.count("LoginCountMgr", null);
        JobHelper.count("RechargeCountMgr", null);
        JobHelper.count("LevelCountMgr", null);

        JobHelper.count("OnlineCountMgr", null);
//        JobHelper.count("OnlineTimesCountMgr", null);
        JobHelper.count("TaskCountMgr", null);
        JobHelper.count("RemainCountMgr", ServerConfigManager.getLogListID());
        JobHelper.count("ArpuCountMgr", ServerConfigManager.getLogListID());
    }




}
