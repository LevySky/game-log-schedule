package com.spc.mysql.count.mgr;

import com.spc.common.AbstractCountManager;
import com.spc.common.Constant;
import com.spc.common.CountProxyHelper;
import com.spc.mysql.count.bean.Arpu;
import com.spc.mysql.count.bean.Login;
import com.spc.mysql.count.bean.Recharge;
import com.spc.mysql.count.bean.Remain;
import com.spc.mysql.count.repository.ArpuRepository;
import com.spc.mysql.count.repository.LoginRepository;
import com.spc.mysql.count.repository.RechargeRepository;
import com.spc.mysql.count.repository.RemainRepository;
import com.spc.mysql.log.JdbcBaseTemplate;
import com.spc.schedule.storage.bean.HandleResult;
import com.spc.usercenter.ServerConfigManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ArpuCountMgr extends AbstractCountManager {

    public static ArpuCountMgr manager;


    @Autowired
    ArpuRepository repository;


    @Autowired
    RechargeRepository rechargeRepository;

    @Autowired
    LoginRepository loginRepository;

    @Autowired
    RemainRepository remainRepository;


    @Autowired
    JdbcBaseTemplate jdbcBaseTemplate;


    public HandleResult etl(Date date, List<Integer> serverIdList) {

        String countDayStr = dateFormat.format(Constant.getLastDay(date));
        try {
            serverIdList = serverIdList != null ? serverIdList : ServerConfigManager.getLogListID();
            List<Arpu> allList = new ArrayList<>();
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


    public void count(Date date, int serverId, List<Arpu> allList) {
        String countDayStr = dateFormat.format(Constant.getLastDay(date));
        Recharge recharge = super.convert(rechargeRepository.findByDate(serverId, countDayStr), new Recharge());
        Login login = super.convert(loginRepository.groupByDate(serverId, countDayStr), new Login());
        Remain remain = remainRepository.findByServerIdAndLogDate(serverId, Constant.getLastDay(date));
        Arpu arpu = new Arpu();
        arpu.setActiveNum(login == null ? 0 : login.getLoginTimes());
        arpu.setRecharge(recharge == null ? 0 : recharge.getCost());
        arpu.setPayNum(recharge == null ? 0 : recharge.getIgnot());
        arpu.setRegisterNum(remain == null ? 0 : remain.getRegisterNum());
        arpu.setServerId(serverId);
        arpu.setLogDate(Constant.getLastDay(date));
        allList.add(arpu);
    }




    public long delete(Date date, List<Integer> serverIdList) {
        String countDayStr = dateFormat.format(Constant.getLastDay(date));
        return manager.repository.deleteByDate(countDayStr + " 00:00:00");
    }


    @PostConstruct
    public void init() {
        manager = this;
        CountProxyHelper.load(this);
    }

}
