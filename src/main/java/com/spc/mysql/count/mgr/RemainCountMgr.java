package com.spc.mysql.count.mgr;

import com.spc.common.Constant;
import com.spc.common.CountProxyHelper;
import com.spc.common.AbstractCountManager;
import com.spc.mysql.count.bean.Login;
import com.spc.mysql.count.bean.Remain;
import com.spc.mysql.count.repository.LoginRepository;
import com.spc.mysql.count.repository.RemainRepository;
import com.spc.mysql.log.JdbcBaseTemplate;
import com.spc.schedule.storage.bean.HandleResult;
import com.spc.usercenter.ServerConfigManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class RemainCountMgr extends AbstractCountManager {


    public static RemainCountMgr manager;
    private final Integer[] remainDay = {1, 2, 3, 4, 5, 6, 13, 29};

    private final int START_DATE_OFFSET = 31;
    private final int END_DATE_OFFSET = 2;

    @Autowired
    RemainRepository repository;

    @Autowired
    JdbcBaseTemplate jdbcBaseTemplate;


    @Autowired
    LoginRepository loginRepository;


    public HandleResult etl(Date date, List<Integer> serverIdList) {

        String countDayStr = dateFormat.format(Constant.getLastDay(date));
        List<Remain> allList = new ArrayList<>();
        try {
            serverIdList = serverIdList != null ? serverIdList : ServerConfigManager.getLogListID();
            serverIdList.forEach(serverId -> count(date, serverId, allList));
            if (!allList.isEmpty()) {
                manager.repository.saveAll(allList);
            }
            return HandleResult.of(true, countDayStr + " 共统计：" + allList.size());
        } catch (Exception e) {
            e.printStackTrace();
            return HandleResult.of(false, countDayStr + "  " + e.getLocalizedMessage());
        }
    }


    public long delete(Date date, List<Integer> serverIdList) {
        String startDataStr = dateFormat.format(Constant.getPrevOffsetDay(date, START_DATE_OFFSET)) + "00:00:00";
        String endDataStr = dateFormat.format(Constant.getPrevOffsetDay(date, END_DATE_OFFSET)) + "23:59:59";
        return 0L;//manager.repository.deleteByDuration(startDataStr,endDataStr);
    }


    public void count(Date date, Integer serverId, List<Remain> allList) {

        Map<Date, List<Remain>> registerMap = new HashMap<>();
        Map<Date, List<Login>> loginMap = new HashMap<>();
        String startDataStr = dateFormat.format(Constant.getPrevOffsetDay(date, START_DATE_OFFSET));
        String endDataStr = dateFormat.format(Constant.getPrevOffsetDay(date, END_DATE_OFFSET));
        String sql = "select playerId,date_format(logDate,'%Y-%m-%d') as logDate from t_log_register where serverId = "+serverId+" and logDate BETWEEN '" + startDataStr + " 00:00:00' and  '" + endDataStr + " 23:59:59'";
        List<Remain> list = manager.jdbcBaseTemplate.findAll(serverId, Remain.class, sql);
        list.forEach(remain -> registerMap.computeIfAbsent(remain.getLogDate(), v -> new ArrayList()).add(remain));
//            System.out.println(registerMap);


        List<Login> loginList = manager.loginRepository.findByDate(startDataStr, endDataStr);
        loginList.forEach(login -> loginMap.computeIfAbsent(login.getLogDate(), v -> new ArrayList()).add(login));

//            System.out.println(loginMap);

        for (Date registDay : registerMap.keySet()) {
            List<Remain> remains = registerMap.get(registDay);
            Remain remainCount = new Remain();
            remainCount.setId(registDay.getTime());
            remainCount.setLogDate(registDay);
            remainCount.setRegisterNum(remains.size());
            remainCount.setServerId(1);
//                System.out.println(registDay);
            for (Integer offset : remainDay) {
                Date offsetDay = Constant.getNextOffsetDay(registDay, offset);
//                    System.out.println(offsetDay);
                List<Login> logins = loginMap.get(offsetDay);

                if (logins == null) {
//                        System.out.println(offsetDay);
                    continue;
                }
                int number = (int) remains.stream().filter(remain -> {
                    return logins.stream().filter(login -> {
                        return remain.getPlayerId().equals(login.getPlayerId());
                    }).count() > 0;
                }).count();
                switch (offset) {
                    case 1:
                        remainCount.setRemain2(number);
                        break;
                    case 2:
                        remainCount.setRemain3(number);
                        break;
                    case 3:
                        remainCount.setRemain4(number);
                        break;
                    case 4:
                        remainCount.setRemain5(number);
                        break;
                    case 5:
                        remainCount.setRemain6(number);
                        break;
                    case 6:
                        remainCount.setRemain7(number);
                        break;
                    case 13:
                        remainCount.setRemain14(number);
                        break;
                    case 29:
                        remainCount.setRemain30(number);
                        break;
                }
            }
            allList.add(remainCount);
        }
    }


    @PostConstruct
    public void init() {
        manager = this;
        CountProxyHelper.load(this);
    }

}
