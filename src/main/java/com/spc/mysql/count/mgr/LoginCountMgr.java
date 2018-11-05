package com.spc.mysql.count.mgr;

import com.spc.common.Constant;
import com.spc.common.CountProxyHelper;
import com.spc.common.AbstractCountManager;
import com.spc.mysql.count.bean.Login;
import com.spc.mysql.count.repository.LoginRepository;
import com.spc.mysql.log.JdbcBaseTemplate;
import com.spc.schedule.storage.bean.HandleResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Service
public class LoginCountMgr extends AbstractCountManager {

    public static LoginCountMgr manager;
    @Autowired
    LoginRepository repository;

    @Autowired
    JdbcBaseTemplate jdbcBaseTemplate;


    public HandleResult etl(Date date, List<Integer> serverIdList) {

        String countDayStr = dateFormat.format(Constant.getLastDay(date));
        try {
            String sql = "select  ip,count(distinct playerId) as loginTimes,playerId,serverId,date_format(logDate,'%Y-%m-%d') as logDate from t_log_login " +
                    "where serverId = # and logDate BETWEEN '" + countDayStr + " 00:00:00' and  '" + countDayStr + " 23:59:59' GROUP BY date_format(logDate,'%Y-%m-%d') ,playerId";
            return super.abstractEtl(manager.jdbcBaseTemplate,manager.repository,Login.class,date,serverIdList,sql);
        } catch (Exception e) {
            e.printStackTrace();
            return HandleResult.of(false, countDayStr + "  " + e.getLocalizedMessage());
        }
    }


    public long delete(Date date, List<Integer> serverIdList) {
        String countDayStr = dateFormat.format(Constant.getLastDay(date));
        return super.del(manager.repository,serverIdList,countDayStr);
    }


    @PostConstruct
    public void init() {
        manager = this;
        CountProxyHelper.load(this);
    }

}
