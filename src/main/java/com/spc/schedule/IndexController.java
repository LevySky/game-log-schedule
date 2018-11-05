package com.spc.schedule;

import com.spc.common.Pagenation;
import com.spc.schedule.storage.bean.HandleResult;
import com.spc.schedule.storage.bean.JobConfig;
import com.spc.schedule.storage.bean.JobInfo;
import com.spc.schedule.storage.repo.JobConfigRepository;
import com.spc.schedule.storage.repo.JobInfoRepository;
import com.spc.usercenter.ServerConfig;
import com.spc.usercenter.ServerConfigManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class IndexController {


    @Autowired
    private QuartzScheduler quartzScheduler;
    @Autowired
    private JobInfoRepository jobInfoRepository;
    @Autowired
    private JobConfigRepository jobConfigRepository;


    @GetMapping("router")
    public String index(String url) {
        return url;
    }

    @PostMapping("/spc/jobInfo/getAll")
    @ResponseBody
    public Page<JobInfo> getJobInfos(@RequestBody Pagenation<JobInfo> pagenation) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("info", ExampleMatcher.GenericPropertyMatchers.contains());
        Example<JobInfo> ex = Example.of(pagenation.getSearch(), matcher);
        PageRequest pageRequest = PageRequest.of(pagenation.getSkip(), pagenation.getPageSize(), new Sort(Sort.Direction.DESC, "endTime"));
        Page<JobInfo> page = jobInfoRepository.findAll(ex, pageRequest);

        for (JobInfo info : page.getContent()) {

            if (info.getJobConfigId() == null) {
                continue;
            }
            JobConfig config = jobConfigRepository.getOne(info.getJobConfigId());
            if (config != null) {
                info.setClassName(config.getGroup());
                info.setCorn(config.getCorn());
                info.setGroup(config.getGroup());
                info.setJobName(config.getJobName());
            }
        }

        return page;
    }

    @PostMapping("/spc/jobConfig/getAll")
    @ResponseBody
    public Page<JobConfig> getConfigs() throws Exception {
        Page<JobConfig> page = jobConfigRepository.findAll(new PageRequest(0, 1000));
        List<JobConfig> list = page.getContent();
        for (JobConfig config : list) {
            String status = quartzScheduler.getJobInfo(config.getJobName(), config.getGroup());
            config.setStatus(status);
        }
        return page;
    }

    @PostMapping("/spc/jobConfig/edit")
    @ResponseBody
    public Map editConfig(@RequestBody Map map) {
        Map resMap = new HashMap();
        try {
            quartzScheduler.editJob(map);
            resMap.put("res", true);
        } catch (Exception e) {
            resMap.put("res", false);
            resMap.put("info", e);
            e.printStackTrace();
        }
        return resMap;
    }

    @PostMapping("/spc/jobConfig/operate")
    @ResponseBody
    public Map operateConfig(@RequestBody Map map) {
        Map resMap = new HashMap();
        try {
            String jobName = map.get("jobName") + "";
            String group = map.get("group") + "";
            switch (map.get("operate").toString()) {
                case "delete":
                    quartzScheduler.deleteJob(jobName, group);
                    break;
                case "resume":
                    quartzScheduler.resumeJob(jobName, group);
                    break;
                case "resumeAll":
                    quartzScheduler.resumeAllJob();
                    break;
                case "pause":
                    quartzScheduler.pauseJob(jobName, group);
                    break;
                case "pauseAll":
                    quartzScheduler.pauseAllJob();
                    break;
            }
            resMap.put("res", true);
        } catch (Exception e) {
            resMap.put("res", false);
            resMap.put("info", e);
            e.printStackTrace();
        }
        return resMap;
    }


    @GetMapping("/spc")
    // @ResponseBody
    public String index() {
//        try {
//           // quartzScheduler
//        } catch (SchedulerException e) {
//            e.printStackTrace();
//        }
        return "main";
    }


    @PostMapping("/spc/jobConfig/add")
    @ResponseBody
    public Map addJob(@RequestBody Map map) {
        Map resMap = new HashMap();
        try {
            quartzScheduler.addJob(map);
            resMap.put("res", true);
        } catch (Exception e) {
            resMap.put("res", false);
            resMap.put("info", e.fillInStackTrace());
            e.printStackTrace();
        }
        return resMap;
    }

    @CrossOrigin(origins = "http://localhost:9090", maxAge = 3600)
    @PostMapping("testPost")
    @ResponseBody
    public String testPost(Map map) {
        return "yes";
    }


    @CrossOrigin(origins = "http://localhost:9090", maxAge = 3600)
    @GetMapping("testGet")
    @ResponseBody
    public Map testGet() {
        Map resMap = new HashMap() {{
            put("res", true);
        }};
        return resMap;
    }

    @PostMapping("/usercenter/getServerConfigs")
    @ResponseBody
    public List<ServerConfig> getServerConfigs() {
        return ServerConfigManager.getList();
    }

    @PostMapping("/usercenter/reload")
    @ResponseBody
    public HandleResult reload() {
        ServerConfigManager.load();
        return HandleResult.of(true, null);
    }


}
