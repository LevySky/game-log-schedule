package com.spc.schedule.storage.service;

import com.alibaba.fastjson.JSONObject;
import com.spc.schedule.storage.bean.JobConfig;
import com.spc.schedule.storage.bean.JobInfo;
import com.spc.schedule.storage.repo.JobConfigRepository;
import com.spc.schedule.storage.repo.JobInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;

@Component
public class JobInfoManager {


    @Autowired
    private JobInfoRepository jobInfoRepository;
    @Autowired
    private JobConfigRepository jobConfigRepository;

    public static JobInfoManager infoManager;

    @PostConstruct
    public void init() {
        infoManager = this;
    }


    public  static void record(Date start, String className, Integer status, String desc,Exception e) {

        JobConfig config = infoManager.jobConfigRepository.findByClassName(className);
        if(config == null){
            config = new JobConfig();
//            config.setId(className);
        }
        JobInfo info = new JobInfo();
        info.setJobConfigId(config.getId());
        info.setStartTime(start);
        info.setStatus(status);
        info.setEndTime(new Date());
//        info.setJobId(config.getId());
        if (e != null) {
            info.setInfo(JSONObject.toJSONString(e.fillInStackTrace()));
        }else{
            info.setInfo(desc == null?config.getInfo():desc);
        }

        infoManager.jobInfoRepository.save(info);
    }
}
