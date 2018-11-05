# game-log-schedule
spring boot + security + quatz + jdbcTemplate : 
1、按时间间隔定时处理读取游戏日志文件，并入库。
2、凌晨进行日志统计聚合入库。 
3、动态调整触发时间间隔，手动回复入库，统计日志
