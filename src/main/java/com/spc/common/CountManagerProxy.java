package com.spc.common;

import com.spc.schedule.storage.bean.HandleResult;

import java.util.Date;
import java.util.List;

public class CountManagerProxy extends AbstractCountManager {


    private BaseCountManager baseCountManager;


    public CountManagerProxy(String mgrName){
        baseCountManager = CountProxyHelper.getProxyMap(mgrName);
    }

    @Override
    public HandleResult etl(Date date, List<Integer> serverIdList) {
        return baseCountManager.etl(date,serverIdList);
    }

    @Override
    public long delete(Date date, List<Integer> serverIdList) {
        return baseCountManager.delete(date,serverIdList);
    }
}
