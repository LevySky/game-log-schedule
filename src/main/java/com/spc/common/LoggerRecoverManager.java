package com.spc.common;

import com.spc.schedule.ThreadHelper;
import com.spc.schedule.storage.bean.HandleResult;
import com.spc.schedule.storage.service.JobInfoManager;
import com.spc.schedule.storage.bean.LogInfo;
import com.spc.schedule.storage.service.LoggerConfigManager;
import com.spc.usercenter.ServerConfig;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class LoggerRecoverManager {


    /**
     * 日志恢复
     *
     * @param logInfo
     */
    public static void execute(LogInfo logInfo) {

        List<ServerConfig> serverList = Constant.getServerList(logInfo.getServers());
        Set<String> fileSet = getLoggerFiles(logInfo);
        List<String> fileDateNames = Constant.findDates(logInfo.getStartTime(), logInfo.getEndTime());
        CyclicBarrier cb = new CyclicBarrier(serverList.size() + 1);
        for (ServerConfig sc : serverList) {
            if (sc == null || StringUtils.isEmpty(sc.getLogFileDir())) {
                continue;
            }
            ThreadHelper.getThreadPollProxy().execute(new Runnable() {
                @Override
                public void run() {
                    for (String fileName : fileSet) {
                        if (StringUtils.isEmpty(fileName)) continue;

                        for (String fileDateName : fileDateNames) {

                            if (StringUtils.isEmpty(fileDateName)) continue;
                            HandleResult hr = LoggerManager.manualReadFile(sc.getId(), sc.getLogFileDir(), fileName, fileDateName, 0L);
                            JobInfoManager.record(new Date(), LoggerRecoverManager.class.getName(), hr.isOk() ? 1 : 2, hr.getResone(), null);
                        }
                    }

                    try {
                        cb.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
        try {
            cb.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }


    private static Set<String> getLoggerFiles(LogInfo logInfo) {
        Set<String> fileSet = new HashSet<>();


        if (Constant.LOGGER_RECOVER_LOGGERFILE_ALL.equals(logInfo.getLogs())) {
            fileSet = LoggerConfigManager.getFileNames();
        } else {
            String[] logFiles = logInfo.getLogs().split(",");
            for (String logFile : logFiles) {
                fileSet.add(logFile);
            }
        }

        return fileSet;
    }


}
