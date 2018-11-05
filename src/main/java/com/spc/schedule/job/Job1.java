package com.spc.schedule.job;

import com.spc.schedule.storage.bean.LoggerConfig;
import com.spc.schedule.storage.repo.LoggerConfigRepository;
import com.spc.schedule.storage.service.JobInfoManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 实现Job接口
 * @author yvan
 *
 */
@Component
public class Job1 implements Job{

    @Autowired
    LoggerConfigRepository loginRepository;
//    @Autowired
//    LoginService loginService;


    public static Job1 job;

    @PostConstruct
    public void init() {
        job = this;
    }

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        Date start = new Date();
        try{
            System.out.println("开始Job1："+new Date());
          //  LogRecover.produce(produceLoginLogger(1000));
           // produceLoginLogger();
            System.out.println("结束Job1："+new Date());
            JobInfoManager.record(start,this.getClass().getName(),1,null,null);
        }catch (Exception e){
            JobInfoManager.record(start,this.getClass().getName(),2,null,e);
            e.printStackTrace();
        }
    }


    public String[] produceLoginLogger(int size){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<LoggerConfig> list = new ArrayList<LoggerConfig>();
        String[] lines = new String[size+1];
        for(int n=0 ;n+1 < size+1;n++){
            String  tableName = "login";
            Integer serverId = (int) (Math.random() * 1000) + 4000;
            Integer userId = (int) (Math.random() * 100000) + 1000000000;
            Integer chanel = (int) (Math.random() * 100) + 1000;
            Integer Level = (int) (Math.random() * 200) + 1;
            Integer vip = (int) (Math.random() * 20) + 1;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 6; i++) {
                char a = (char) (Math.random() * (90 - 65) + 65);
                sb.append(a);
            }
            String MCC = sb.toString();
            StringBuilder sb1 = new StringBuilder();
            for (int i = 0; i < 4; i++) {
                int ip =  (int) (Math.random() * 100) + 100;
                sb1.append(ip).append(".");
            }
            String ip = sb1.toString().substring(0,sb1.length()-1);
            StringBuffer sb2 = new StringBuffer();
            sb2.append(sdf.format(new Date())).append(",");
            sb2.append(tableName).append(",");
            sb2.append(serverId).append(",");
            sb2.append(userId).append(",");
            sb2.append(chanel).append(",");
            sb2.append(Level).append(",");
            sb2.append(vip).append(",");
            sb2.append(MCC).append(",");
            sb2.append(ip).append(",");
            lines[n] = "2018-05-09 15:02:19,196-1-{\"playerId\":122303022020854066,\"ip\":\"/218.17.160.163:31471\",\"serverId\":1,\"time\":1525849339196,\"type\":1}";
            lines[n+1] = "2018-05-09 15:05:53,677-2-{\"playerId\":122303022020854066,\"ip\":\"/218.17.160.163:31471\",\"loginTime\":1525849339196,\"logoutTime\":1525849553677,\"onlineTime\":214,\"serverId\":1,\"type\":2}";
            try {
                //list.add(job.loginService.str2Bean(sb2.toString()));
               // job.loginRepository.saveAll(list);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return lines;
    }


}
