package com.spc.common;

import com.spc.mysql.log.JdbcBaseTemplate;
import com.spc.schedule.storage.bean.HandleResult;
import com.spc.schedule.storage.bean.LoggerConfig;
import com.spc.schedule.storage.bean.LoggerPosition;
import com.spc.schedule.storage.bean.LoggerPositionHistory;
import com.spc.schedule.storage.repo.LoggerPositionHistoryRepository;
import com.spc.schedule.storage.repo.LoggerPositionRepository;
import com.spc.schedule.storage.service.LoggerConfigManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class LoggerManager {


    public static LoggerManager manager;
    private static Logger log = LoggerFactory.getLogger(LoggerManager.class);
    @Autowired
    LoggerPositionRepository logPosRepository;
    @Autowired
    LoggerPositionHistoryRepository logPosHisRepository;


    @Autowired
    JdbcBaseTemplate baseTemplate;


    private static void recordPosition(Integer serverId, String fileName, long position, Boolean onlySaveHistory) {
        LoggerPosition lp = manager.logPosRepository.findByServerIdAndFileName(serverId, fileName);
        if (lp == null) {
            lp = new LoggerPosition();
            lp.setFileName(fileName);
            lp.setServerId(serverId);
        }

        if (lp.getPosition() >= position) {
            return;
        }

        lp.setPosition(position);
        lp.setUpdateTime(new Date());

        if (!onlySaveHistory) {
            manager.logPosRepository.save(lp);
        }

        LoggerPositionHistory lph = new LoggerPositionHistory();
        lph.convert2History(lp);
        manager.logPosHisRepository.save(lph);

    }

    /**
     * 手动处理
     *
     * @param serverId
     * @param dir
     * @param fileName
     * @param date
     * @return
     */
    public static HandleResult manualReadFile(Integer serverId, String dir, String fileName, String date, long skip) {
        InputStream inputStream = null;
        InputStreamReader isr = null;
        BufferedReader reader = null;
        String desc = serverId + "--" + dir + fileName + (StringUtils.isNotEmpty(date) ? "." + date : "");
        String path = dir + fileName;

        if (StringUtils.isNotEmpty(date)) {
            path += "." + date;
        }
        try {
            HttpURLConnection httpconn = getHttpConn(path);
            if (httpconn == null || httpconn.getResponseCode() >= 400) {
                return HandleResult.of(false, desc + " http code: " + httpconn.getResponseCode());
            }
            inputStream = httpconn.getInputStream();
            inputStream.skip(skip);

            desc += "  size:" + (httpconn.getContentLength() - skip);
            log.info("http code:{},content length:{},last content length:{},path:{}", httpconn.getResponseCode(), httpconn.getContentLength(), skip, desc);
            isr = new InputStreamReader(inputStream);
            reader = new BufferedReader(isr);
            List<LoggerConfig> lcs = LoggerConfigManager.getListByFile(fileName);
            Map<String, List<String>> filterMap = filter(reader, lcs);
            parseFile(serverId, filterMap, lcs, true);
            // recordPosition(serverId, fileName, httpconn.getContentLength(),true);
            httpconn.disconnect();
            return HandleResult.of(true, desc);

        } catch (Exception e) {
            e.printStackTrace();
            return HandleResult.of(false, desc + "  resone: " + e.getLocalizedMessage());
        } finally {
            close(inputStream, isr, reader);
        }
    }

    /**
     * 定时处理
     *
     * @param serverId
     * @param dir
     * @param fileName
     * @return
     */
    public static HandleResult autoReadFile(Integer serverId, String dir, String fileName) {

        InputStream inputStream = null;
        InputStreamReader isr = null;
        BufferedReader reader = null;
        boolean result = false;
        long skip = 0L;
        String desc = serverId + "..." + dir + fileName;
        String path = dir + fileName;

        try {
            HttpURLConnection httpconn = getHttpConn(path);
            if (httpconn == null || httpconn.getResponseCode() >= 400) {
                return HandleResult.of(result, desc + " http code: " + httpconn.getResponseCode());
            }
            inputStream = httpconn.getInputStream();
            skip = getLastPos4Daily(serverId, fileName);
            inputStream.skip(skip);
            isr = new InputStreamReader(inputStream);
            reader = new BufferedReader(isr);

            List<LoggerConfig> lcs = LoggerConfigManager.getListByFile(fileName);

            Map<String, List<String>> filterMap = filter(reader, lcs);
            switch (parseFile(serverId, filterMap, lcs, false)) {
                case Constant.LOGGER_PARSE_OK:
                    recordPosition(serverId, fileName, httpconn.getContentLength(), false);
                    result = true;
                    break;
                case Constant.LOGGER_PARSE_EMPTY:
                    desc = "暂无数据写入..." + desc;
                    result = true;
                    break;
                case Constant.LOGGER_PARSE_NOT_SAME_DAY:
                    desc = "旧文件，未切割..." + desc;
                    break;
            }

            desc += "  size:" + (httpconn.getContentLength() - skip);
            log.info("http code:{},content length:{},last content length:{},path:{}", httpconn.getResponseCode(), httpconn.getContentLength(), skip, desc);
            httpconn.disconnect();
            return HandleResult.of(result, desc);

        } catch (Exception e) {
            e.printStackTrace();
            return HandleResult.of(false, desc + "  resone: " + e.getLocalizedMessage());
        } finally {
            close(inputStream, isr, reader);
        }

    }

    private static void close(InputStream is, InputStreamReader isr, BufferedReader br) {
        try {
            if (is != null) {
                is.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (br != null) {
                br.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static HttpURLConnection getHttpConn(String path) {

        HttpURLConnection httpconn = null;
        try {
            URL url = new URL(path);
//            System.out.println(path);

            httpconn = (HttpURLConnection) url.openConnection();
            return httpconn;
        } catch (IOException e) {
            if (httpconn != null) {
                httpconn.disconnect();
            }
            e.printStackTrace();
            return null;
        }
    }


    public static long getLastPos4Daily(Integer serverId, String fileName) {

        LoggerPosition lp = manager.logPosRepository.findByServerIdAndFileName(serverId, fileName);
        return lp != null ? lp.getPosition() : 0L;
    }

    public static long getLastPos4Last(Integer serverId, String fileName) {

//        TypedAggregation<LoggerPositionHistory> agg = Aggregation.newAggregation(
//                LoggerPositionHistory.class
//                , Aggregation.match(getYesterdayCriteria(serverId, fileName))
//                , Aggregation.group("serverId", "fileName").max("position").as("position")
//                , Aggregation.skip(0L)
//                , Aggregation.limit(1)
//        );
//        agg.withOptions(AggregationOptions.builder().allowDiskUse(true).build());
//        AggregationResults<LoggerPositionHistory> aggRsult = manager.countMongoTemplate.aggregate(agg, "loggerPositionHistory", LoggerPositionHistory.class);
//        if (aggRsult.getMappedResults() != null) {
//           for(LoggerPositionHistory history : aggRsult.getMappedResults()){
////               System.out.println("getLastPos4Last-----sid: " + serverId + "  fname:   " + fileName + "  pos:   " + history.getPosition());
//               return history.getPosition();
//           }
//        }
        return 0L;
    }


    /**
     * 解析文件并入库
     * @param serverId
     * @param mapList
     * @param lcs
     * @param isLast
     * @return
     */
    private static int parseFile(Integer serverId, Map<String, List<String>> mapList, List<LoggerConfig> lcs, boolean isLast) {


        if (mapList.isEmpty()) {
            return Constant.LOGGER_PARSE_EMPTY;
        }

        if (!isCurrentDayLog(mapList) && !isLast) {
            return Constant.LOGGER_PARSE_NOT_SAME_DAY;
        }

        for (String collName : mapList.keySet()) {
            LoggerConfig lc = getLoggerConfig(collName, lcs);
            if (lc == null) {
                log.error("解析：{},{}失败", collName, lcs);
                continue;
            }
            List<String> list = mapList.get(collName);
            if (list != null && !list.isEmpty()) {
                manager.baseTemplate.insert(serverId, collName, list, lc.getSpliteFlag());
                log.info("解析：{},{}", lc.getCollectionName(), list.size());
            }
        }

        return Constant.LOGGER_PARSE_OK;

    }


    public static boolean isCurrentDayLog(Map<String, List<String>> mapList) {


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = null;
        for (String key : mapList.keySet()) {
            List<String> list = mapList.get(key);
            if (list != null && !list.isEmpty()) {
                str = list.get(0);
            }
        }

        Date date = null;
        try {

            String[] arr = str.split("-\\{");

            if (arr.length < 2) {
                return false;
            }

            System.out.println(arr[0]);
            date = sdf.parse(arr[0].substring(0, 19));
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return Constant.isSameDay(date, new Date());
    }

    /**
     * 根据日志类型过滤
     * @param br
     * @param lcs
     * @return
     */
    private static Map<String, List<String>> filter(BufferedReader br, List<LoggerConfig> lcs) {

        Map<String, List<String>> mapList = new HashMap<String, List<String>>();
        try {
            String line = null;
            while ((line = br.readLine()) != null) {
                for (LoggerConfig lc : lcs) {
                    if (line.indexOf(lc.getFilterFlag()) < 0) {
                        continue;
                    }
                    List<String> list = mapList.get(lc.getCollectionName());
                    if (list == null) {
                        list = new ArrayList<>();
                    }
                    list.add(line);
                    mapList.put(lc.getCollectionName(), list);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return mapList;
    }


    public static LoggerConfig getLoggerConfig(String collName, List<LoggerConfig> lcs) {
        for (LoggerConfig lc : lcs) {
            if (collName.equals(lc.getCollectionName())) {
                return lc;
            }
        }
        return null;
    }

    @PostConstruct
    public void init() {
        manager = this;
    }

}
