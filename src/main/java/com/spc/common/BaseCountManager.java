package com.spc.common;

import com.spc.schedule.storage.bean.HandleResult;

import java.util.Date;
import java.util.List;

public interface  BaseCountManager {
    HandleResult etl(Date date, List<Integer> serverIdList);
    long delete(Date date, List<Integer> serverIdList);
}
