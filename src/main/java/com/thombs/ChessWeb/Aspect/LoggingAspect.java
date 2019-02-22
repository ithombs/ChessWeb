package com.thombs.ChessWeb.Aspect;

import java.sql.Timestamp;
import java.util.Date;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.thombs.ChessWeb.DataAccess.ActivityAuditService;
import com.thombs.ChessWeb.Models.ActivityAudit;

@Component
@Aspect
public class LoggingAspect {
	private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

	@Autowired
	private ActivityAuditService aaService;
	
	
	@Around("@annotation(level)")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint, LoggerTest level) throws Throwable {
        if(level.level() >= 0) {
            //Default Object that we can use to return to the consumer
            Object returnObject = null;
            ActivityAudit aa = new ActivityAudit();
 
            try {
            	logger.info("Logger level = " + level.level());
                logger.info("Logging from - [" + joinPoint.getSignature().getName() + "] - START");
                aa.setStartTime(new Timestamp(System.currentTimeMillis()));
                aa.setActivityName(level.activityName());
                
                returnObject = joinPoint.proceed();
                
                aa.setEndTime(new Timestamp(System.currentTimeMillis()));
                aaService.saveActivityAudit(aa);
            } catch (Throwable throwable) {
                //Here we can catch and modify any exceptions that are called
                //We could potentially not throw the exception to the caller and instead return "null" or a default object.
                throw throwable;
            } finally {
                //If we want to be sure that some of our code is executed even if we get an exception
            	logger.info("Logging from - [" + joinPoint.getSignature().getName() + "] - END");
            }
            return returnObject;
        }
        else{
            return joinPoint.proceed();
        }
    }
}
