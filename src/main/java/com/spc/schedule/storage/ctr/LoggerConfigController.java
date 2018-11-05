package com.spc.schedule.storage.ctr;


import com.spc.schedule.storage.bean.HandleResult;
import com.spc.schedule.storage.bean.LoggerConfig;
import com.spc.schedule.storage.repo.LoggerConfigRepository;
import com.spc.schedule.storage.service.LoggerConfigManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;
import java.util.Set;

@RequestMapping("logger")
@Controller
public class LoggerConfigController {
    @Autowired
    LoggerConfigRepository loggerConfigRepository;


    @PostMapping("get")
    @ResponseBody
    public List<LoggerConfig> get(){
        return loggerConfigRepository.findAll();
    }


    @PostMapping("edit")
    @ResponseBody
    public HandleResult edit(@RequestBody  LoggerConfig loggerConfig){
        try{
            loggerConfig.setUpdateTime(new Date());
            loggerConfigRepository.save(loggerConfig);
            LoggerConfigManager.load();
            return HandleResult.of(true,null);
        }catch (Exception e){

            e.printStackTrace();
            return HandleResult.of(false,e.getLocalizedMessage());
        }
    }

    @PostMapping("delete")
    @ResponseBody
    public HandleResult delete(@RequestBody LoggerConfig loggerConfig){
        try{
            loggerConfigRepository.delete(loggerConfig);
            LoggerConfigManager.load();
            return HandleResult.of(true,"");
        }catch (Exception e){
            e.printStackTrace();
            return HandleResult.of(false,e.getLocalizedMessage());
        }
    }

    @PostMapping("getFileNames")
    @ResponseBody
    public Set<String> getFileNames(){
        return LoggerConfigManager.getFileNames();
    }



}
