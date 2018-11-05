package com.spc.schedule.storage.service;

import com.spc.schedule.storage.bean.LogInfo;
import com.spc.schedule.storage.repo.LogInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.SimpleTimeZone;

@Service
public class LogInfoService  {

    @Autowired
    LogInfoRepository logInfoRepository;

    public List<LogInfo> getAll(Map map){
        return logInfoRepository.findAll();
    }


    public boolean add(LogInfo info){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
        try {
            logInfoRepository.save(info);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(LogInfo info){
        try {
            logInfoRepository.delete(info);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean execute(LogInfo info){
        try {

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
