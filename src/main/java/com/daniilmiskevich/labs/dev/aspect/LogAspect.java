package com.daniilmiskevich.labs.dev.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
public class LogAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);

    @Before("execution(public * com.daniilmiskevich.labs.*.controller.*.*(..))")
    public void controllerMethodAccessed(JoinPoint joinPoint) {
        LOGGER.info("Controller method accessed: {}.{}",
            joinPoint.getTarget().getClass().getName(),
            joinPoint.getSignature().getName());
    }

    @AfterReturning(
        pointcut = "execution(public * com.daniilmiskevich.labs.*.controller.*.*(..))",
        returning = "result")
    public void controllerMethodReturned(JoinPoint joinPoint, Object result) {
        LOGGER.trace("Controller method returned: {}.{}",
            joinPoint.getTarget().getClass().getName(),
            joinPoint.getSignature().getName());
    }

    @AfterThrowing(
        pointcut = "execution(public * com.daniilmiskevich.labs.*.controller.*.*(..))",
        throwing = "exception")
    public void controllerMethodThrew(JoinPoint joinPoint, Exception exception) {
        LOGGER.warn("Controller method threw: {}.{} -> {}",
            joinPoint.getTarget().getClass().getName(),
            joinPoint.getSignature().getName(),
            exception.getClass().getName());
    }

    @AfterReturning(
        pointcut = "execution(public * com.daniilmiskevich.labs.*.repository.*.*(..))",
        returning = "result")
    public void repositoryMethodReturned(JoinPoint joinPoint, Object result) {
        LOGGER.trace("Repository method returned: {}.{}",
            joinPoint.getTarget().getClass().getName(),
            joinPoint.getSignature().getName());
    }

    @AfterThrowing(
        pointcut = "execution(public * com.daniilmiskevich.labs.*.repository.*.*(..))",
        throwing = "exception")
    public void repositoryMethodThrew(JoinPoint joinPoint, Exception exception) {
        LOGGER.warn("Repository method threw: {}.{} -> {}",
            joinPoint.getTarget().getClass().getName(),
            joinPoint.getSignature().getName(),
            exception.getClass().getName());
    }

    @AfterReturning(pointcut = "execution(public * com.daniilmiskevich.labs.*.cache.*.put*(..))")
    public void cachePut(JoinPoint joinPoint) {
        LOGGER.info("Cache entry added: {}", joinPoint.getTarget().getClass().getName());
    }

    @AfterReturning(
        pointcut = "execution(public * com.daniilmiskevich.labs.*.cache.*.get*(..))",
        returning = "result")
    public void cacheGot(JoinPoint joinPoint, Object result) {
        if (result == null || (result instanceof List && ((List<?>) result).isEmpty())) {
            LOGGER.info("Cache miss!");
        } else {
            LOGGER.info("Cache entry taken: {}", joinPoint.getTarget().getClass().getName());
        }

    }

}
