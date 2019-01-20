package com.thombs.ChessWeb.Aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggingAspect {
	private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
/*	@Pointcut("@annotation(level)")
    public void annotationPointCutDefinition(LoggerTest level){
    }
	
	@Pointcut("execution(* *(..))")
    public void atExecution(){}
*/
	
	@Around("@annotation(level)")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint, LoggerTest level) throws Throwable {
        if(level.level() >= 0) {
            //Default Object that we can use to return to the consumer
            Object returnObject = null;
 
            try {
            	logger.info("Logger level = " + level.level());
                logger.info("Logging from - [" + joinPoint.getSignature().getName() + "] - START");
                //We choose to continue the call to the method in question
                returnObject = joinPoint.proceed();
                //If no exception is thrown we should land here and we can modify the returnObject, if we want to.
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
 
    /*
     * @After("annotationPointCutDefinition(level) && atExecution()")
    //JointPoint = the reference of the call to the method
    public void printNewLine(JoinPoint pointcut, LoggerTest level){
        //Just prints new lines after each method that's executed in
        System.out.print("\n\r");
    }
    */
}
