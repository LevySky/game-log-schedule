package com.spc.aop;

import com.spc.mysql.db.dynamic.DataBaseContextHolder;
import com.spc.mysql.db.dynamic.DataBaseType;
import com.spc.usercenter.ServerConfig;
import com.spc.usercenter.ServerConfigManager;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * @author levy
 * @version [v1.0, 2016年12月8日]
 */

@Component
@Aspect
public class LoggerDataSourceAop {
	
	private Logger log = LoggerFactory.getLogger(LoggerDataSourceAop.class);
	@Pointcut("execution(* com.spc.mysql.log..*.*(..))")
    public void listenser(){
    }

    @Before("listenser()")
    public void before(JoinPoint joinPoint){
		String methodName = joinPoint.getSignature().getName();
		String targetClassName = joinPoint.getSignature().getDeclaringType().getSimpleName();
		Object[] arguments = joinPoint.getArgs();
		Integer serverId = (int)arguments[0];

		ServerConfig sc = ServerConfigManager.getServerConfig(serverId);
		if(sc == null){

			return;
		}

//		log.info("Aop 方法 {},{},{}，{}",targetClassName,methodName,serverId,sc.getDbUrl());
		DataBaseContextHolder.setObject(DataBaseType.logger_db);
		DataBaseContextHolder.setServerConfig(sc);
    }
      

}
