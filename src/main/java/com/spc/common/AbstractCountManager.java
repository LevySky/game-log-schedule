package com.spc.common;

import com.alibaba.fastjson.JSONObject;
import com.spc.mysql.count.repository.BaseRepository;
import com.spc.mysql.log.JdbcBaseTemplate;
import com.spc.schedule.storage.bean.HandleResult;
import com.spc.usercenter.ServerConfigManager;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public  abstract class AbstractCountManager implements BaseCountManager {

    public final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");





    protected  long del(BaseRepository repository, List<Integer> ids, String dateStr){
        ids = ids != null ? ids : ServerConfigManager.getLogListID();
        long size = 0;
        for(Integer  serverId: ids){
            size += repository.deleteByDate(serverId,dateStr);
        }
        return size;
    }


    protected <T> HandleResult abstractEtl(JdbcBaseTemplate jdbcBaseTemplate, Object repository,Class<T> e, Date date, List<Integer> serverIdList, String sql){
        String countDayStr = dateFormat.format(Constant.getLastDay(date));
        List<T> allList = new ArrayList<>();
        serverIdList = serverIdList != null ? serverIdList : ServerConfigManager.getLogListID();
        serverIdList.forEach(serverId->{
            List<T> list = jdbcBaseTemplate.findAll(serverId, e, replace(sql,serverId));
            if(list != null  && !list.isEmpty()){
                allList.addAll(list);
            }
        });
        if (!allList.isEmpty()) {
            ((JpaRepository)repository).saveAll(allList);
        }
        return HandleResult.of(true, countDayStr + " 共统计：" + allList.size());
    }



    public String replace(String str,Integer serverId){
        return str.replace("#",""+serverId);
    }


    public <T> T convert(JSONObject jsonObject, T entity) {
        if (jsonObject != null) {
            Object value = null;
            for (String key : jsonObject.keySet()) {
                value = jsonObject.get(key);

                if (value instanceof BigInteger) {
                    value = jsonObject.getInteger(key);
                }
                if (value == null) {
                    continue;
                }
                try {
                    PropertyUtils.setProperty(entity, key, value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return entity;

        }
        return null;
    }
}
