package com.spc.mysql.count.repository;

public interface BaseRepository {
    int deleteByDate(Integer serverId,String dateStr);
}
