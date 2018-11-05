package com.spc.common;

import com.spc.usercenter.ServerConfig;
import com.spc.usercenter.ServerConfigManager;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public class Constant {


    public final static String LOGGER_RECOVER_SERVER_ALL = "all";
    public final static String LOGGER_RECOVER_LOGGERFILE_ALL = "all";

    public final static int LOGGER_PARSE_NOT_SAME_DAY = -1;
    public final static int LOGGER_PARSE_EMPTY = 0;
    public final static int LOGGER_PARSE_OK = 1;

    public final static Integer READER_LAST_DAY_MINUTE_FLAG = 3;

    public static List<String> findDates(Date dBegin, Date dEnd) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        List<String> list = new ArrayList<String>();

        list.add(sdf.format(dBegin));
        Calendar calBegin = Calendar.getInstance();

        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();

        calEnd.setTime(dEnd);

        while (dEnd.after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            list.add(sdf.format(calBegin.getTime()));
        }
        return list;
    }

    public static List<Date> findDatesAddOne(Date dBegin, Date dEnd) {


        List<Date> list = new ArrayList<Date>();

        Calendar ca = Calendar.getInstance();
        ca.setTime(dBegin);
        list.add(ca.getTime());

        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(dEnd);

        while (dEnd.after(calBegin.getTime())) {
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            list.add(calBegin.getTime());
        }


        List<Date> listAddOne = new ArrayList<Date>();
        for (Date date : list) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            listAddOne.add(calendar.getTime());
        }
        return listAddOne;
    }


    public static Date getPrevOffsetDay(Date date,int offset) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -offset);
        return calendar.getTime();
    }

    public static Date getNextOffsetDay(Date date,int offset) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, offset);
        return calendar.getTime();
    }

    public static Date getLastDay(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTime();
    }

    public static String getLastDayString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return sdf.format(calendar.getTime());
    }

    public static int getHourOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinOfHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.MINUTE);
    }

    public static Date getFirstDayTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTime();
    }

    public static Date getDayHourTime(Date date,int hour,boolean isStart) {

        int startMin = 0;
        int endMin = 29;
        if(hour % 2 != 0){
            startMin = 30;
            endMin = 59;
        }
        hour = hour / 2;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        if(isStart){
            calendar.set(Calendar.MINUTE, startMin);
            calendar.set(Calendar.SECOND, 0);
        }else{
            calendar.set(Calendar.MINUTE, endMin);
            calendar.set(Calendar.SECOND, 59);
        }
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTime();
    }


    public static Date getMonthFirstDayTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH,0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTime();
    }


    public static Date getLastDayTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND,999);
        return calendar.getTime();
    }


    public static List<ServerConfig> getServerList(String servers) {
        List<ServerConfig> serverList = new ArrayList<>();

        if (LOGGER_RECOVER_SERVER_ALL.equals(servers)) {
            serverList = ServerConfigManager.getList();
        } else {
            String[] ids = servers.split(",");
            for (String id : ids) {
                ServerConfig sc = ServerConfigManager.getServerConfig(Integer.parseInt(id));
                if (sc != null) {
                    serverList.add(sc);
                }
            }
        }
        return serverList;
    }

    public static String getRootPath() {
        String line= File.separator;
        String path=Thread.currentThread().getContextClassLoader().getResource("").toString();
        //windows下
        if("\\".equals(line)){
            path = path.replace("/", "\\");  // 将/换成\\
        }
        //linux下
        if("/".equals(line)){
            path = path.replace("\\", "/");
        }

        return path;
    }


    public static List<String> getLogCountExecClass() throws Exception {
        String str = System.getProperty("user.dir") + "/src/main/java/com/spc/count/manager";
        System.out.println(str);
        Path path = Paths.get(str);
        Stream<Path> ps = Files.list(path);
        List<String> list = new ArrayList<>();

        ps.forEach(p -> {

            String item = p.getFileName().toString();
            item = item.substring(0, item.length() - 5);
            //  System.out.println(item);
            list.add(item);
        });
        return list;
    }

    public static boolean isSameDay(Date date1, Date date2) {
        Calendar calDateA = Calendar.getInstance();
        calDateA.setTime(date1);

        Calendar calDateB = Calendar.getInstance();
        calDateB.setTime(date2);

        return calDateA.get(Calendar.YEAR) == calDateB.get(Calendar.YEAR)
                && calDateA.get(Calendar.MONTH) == calDateB.get(Calendar.MONTH)
                && calDateA.get(Calendar.DAY_OF_MONTH) == calDateB
                .get(Calendar.DAY_OF_MONTH);
    }


    public static void main(String[] args) throws Exception {
//        System.out.println(getFirstDayTime(new Date()));
//        System.out.println(getLastDayTime(new Date()));
//
//
//        System.out.println(findDatesAddOne(new Date(), new Date()));
//        getLogCountExecClass();


        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(sdf.format(getMonthFirstDayTime(new Date())));

    }




}
