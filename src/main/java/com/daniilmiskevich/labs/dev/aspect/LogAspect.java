package com.daniilmiskevich.labs.dev.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Before("execution(public * com.daniilmiskevich.labs.*.controller.*.*(..))")
    public void controllerMethodAccessed(JoinPoint joinPoint) {
        logger.info("Controller method accessed: {}.{}",
            joinPoint.getTarget().getClass().getName(),
            joinPoint.getSignature().getName());
    }

    @AfterReturning(
        pointcut = "execution(public * com.daniilmiskevich.labs.*.controller.*.*(..))",
        returning = "result")
    public void controllerMethodReturned(JoinPoint joinPoint, Object result) {
        logger.info("Controller method returned: {}.{} -> {}",
            joinPoint.getTarget().getClass().getName(),
            joinPoint.getSignature().getName(),
            result);
    }

    @AfterThrowing(
        pointcut = "execution(public * com.daniilmiskevich.labs.*.controller.*.*(..))",
        throwing = "exception")
    public void controllerMethodThrew(JoinPoint joinPoint, Exception exception) {
        logger.error("Controller method threw: {}.{} -> {}",
            joinPoint.getTarget().getClass().getName(),
            joinPoint.getSignature().getName(),
            exception.getClass().getName());
    }

}
