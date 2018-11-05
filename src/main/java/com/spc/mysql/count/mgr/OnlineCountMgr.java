package com.spc.mysql.count.mgr;

import com.spc.common.Constant;
import com.spc.common.CountProxyHelper;
import com.spc.common.AbstractCountManager;
import com.spc.mysql.count.bean.Online;
import com.spc.mysql.count.repository.OnlineRepository;
import com.spc.mysql.log.JdbcBaseTemplate;
import com.spc.schedule.storage.bean.HandleResult;
import com.spc.usercenter.ServerConfigManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class OnlineCountMgr extends AbstractCountManager {

    public static OnlineCountMgr manager;


    @Autowired
    OnlineRepository repository;

    @Autowired
    JdbcBaseTemplate jdbcBaseTemplate;


    public HandleResult etl(Date date, List<Integer> serverIdList) {

        String countDayStr = dateFormat.format(Constant.getLastDay(date));
        try {
            serverIdList = serverIdList != null ? serverIdList : ServerConfigManager.getLogListID();
            List<Online> allList = new ArrayList<>();
            serverIdList.forEach(serverId->countByHour(date,serverId,allList));
            if (!allList.isEmpty()) {
                manager.repository.saveAll(allList);
            }
            return HandleResult.of(true, countDayStr + " 共统计：" + allList.size());
        } catch (Exception e) {
            e.printStackTrace();
            return HandleResult.of(false, countDayStr + "  " + e.getLocalizedMessage());
        }
    }


    public void countByHour(Date date, int serverId, List<Online> allList) {

        String countDayStr = dateFormat.format(Constant.getLastDay(date));
        String sql = "select  * from t_log_online where serverId = "+serverId+" and logDate BETWEEN '" + countDayStr + " 00:00:00' and  '" + countDayStr + " 23:59:59'";
        List<Online> dbList = manager.jdbcBaseTemplate.findAll(serverId, Online.class, sql);
//        Map<Date, List<Long>> map = new HashMap<>();
        Date countDay = Constant.getLastDay(date);
        gt:
        for (int i = 0; i < 48; i++) {

            Date start = Constant.getDayHourTime(countDay, i, true);
            Date end = Constant.getDayHourTime(countDay, i, false);

            List<Long> list = new ArrayList<>();
            for (Online doc : dbList) {

                Date loginTime = doc.getLoginTime();
                Date logoutTime = doc.getLogoutTime();
                try {

                    if (!(loginTime.after(end) || logoutTime.before(start))) {
                        long playerId = Long.parseLong(doc.getPlayerId());
                        if (!list.contains(playerId)) {
                            list.add(playerId);
                        }
                    }
                } catch (Exception e) {
                    System.out.println(loginTime + "   " + logoutTime);
                    continue gt;
                }
            }
//            map.put(start, list);
            Online onlineCount = new Online();
            onlineCount.setOnlineNum((long) list.size());
            onlineCount.setServerId(serverId);
            onlineCount.setOriginLogDate(start);
            allList.add(onlineCount);
        }
    }

    public long delete(Date date, List<Integer> serverIdList) {
        String countDayStr = dateFormat.format(Constant.getLastDay(date));
        serverIdList = serverIdList != null ? serverIdList : ServerConfigManager.getLogListID();
        long size = 0;
        for(Integer  serverId: serverIdList){
            size += repository.deleteByDates(serverId,countDayStr+" 00:00:00",countDayStr+" 23:59:59");
        }
        return size;
    }


    @PostConstruct
    public void init() {
        manager = this;
        CountProxyHelper.load(this);
    }

}
