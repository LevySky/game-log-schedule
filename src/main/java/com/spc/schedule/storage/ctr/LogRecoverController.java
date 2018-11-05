package com.spc.schedule.storage.ctr;

import com.spc.common.CountProxyHelper;
import com.spc.common.LoggerCountRecoverManager;
import com.spc.common.LoggerRecoverManager;
import com.spc.schedule.storage.bean.HandleResult;
import com.spc.schedule.storage.bean.LogCountInfo;
import com.spc.schedule.storage.bean.LogInfo;
import com.spc.schedule.storage.repo.LogCountInfoRepository;
import com.spc.schedule.storage.repo.LogInfoRepository;
import com.spc.schedule.storage.service.LogInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.SimpleTimeZone;

@Controller
public class LogRecoverController {

    @Autowired
    LogInfoRepository logInfoRepository;

    @Autowired
    LogCountInfoRepository countRepository;

    @Autowired
    LogInfoService logInfoService;

    @PostMapping("/spc/log/getAll")
    @ResponseBody
    public List<LogInfo> getAll(@RequestBody Map map) {

        return logInfoService.getAll(map);
    }

    @PostMapping("/spc/log/edit")
    @ResponseBody
    public HandleResult add(@RequestBody LogInfo info) {
        return HandleResult.of(logInfoService.add(info),null);
    }

    @PostMapping("/spc/log/delete")
    @ResponseBody
    public HandleResult delete(@RequestBody LogInfo info) {
        return HandleResult.of(logInfoService.delete(info),null);
    }

    @PostMapping("/spc/log/exec")
    @ResponseBody
    public HandleResult exec(@RequestBody LogInfo info) {
        LoggerRecoverManager.execute(info);
        return HandleResult.of(true,null);
    }



    @PostMapping("/spc/logCount/getAll")
    @ResponseBody
    public List<LogCountInfo> getLogCountAll() {

        return countRepository.findAll();
    }

    @PostMapping("/spc/logCount/edit")
    @ResponseBody
    public HandleResult addLogCount(@RequestBody LogCountInfo info) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
        countRepository.save(info);
        return HandleResult.of(true,null);
    }

    @PostMapping("/spc/logCount/delete")
    @ResponseBody
    public HandleResult deleteLogCount(@RequestBody LogCountInfo info) {
        countRepository.delete(info);
        return HandleResult.of(true,null);
    }

    @PostMapping("/spc/logCount/exec")
    @ResponseBody
    public HandleResult logCountExec(@RequestBody LogCountInfo info) {
        LoggerCountRecoverManager.execute(info);
        return HandleResult.of(true,null);
    }

    @PostMapping("/spc/logCount/getExecClass")
    @ResponseBody
    public List<String> getExecClass() throws Exception{

        return CountProxyHelper.getAllCountMgrNames();
    }
}
