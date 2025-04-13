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
        var signature = joinPoint.getSignature();
        LOGGER.info("Controller method accessed: {}::{}",
            signature.getDeclaringTypeName(),
            signature.getName());
    }

    @AfterReturning(
        pointcut = "execution(public * com.daniilmiskevich.labs.*.controller.*.*(..))",
        returning = "result")
    public void controllerMethodReturned(JoinPoint joinPoint, Object result) {
        var signature = joinPoint.getSignature();
        LOGGER.trace("Controller method returned: {}::{}",
            signature.getDeclaringTypeName(),
            signature.getName());
    }

    @AfterThrowing(
        pointcut = "execution(public * com.daniilmiskevich.labs.*.controller.*.*(..))",
        throwing = "exception")
    public void controllerMethodThrew(JoinPoint joinPoint, Exception exception) {
        var signature = joinPoint.getSignature();
        LOGGER.warn("Controller method threw: {}::{} -> {}",
            signature.getDeclaringTypeName(),
            signature.getName(),
            exception.getClass().getName());
    }

    @AfterReturning(
        pointcut = "execution(public * com.daniilmiskevich.labs.*.repository.*.*(..))",
        returning = "result")
    public void repositoryMethodReturned(JoinPoint joinPoint, Object result) {
        var signature = joinPoint.getSignature();
        LOGGER.trace("Repository method returned: {}::{}",
            signature.getDeclaringTypeName(),
            signature.getName());
    }

    @AfterThrowing(
        pointcut = "execution(public * com.daniilmiskevich.labs.*.repository.*.*(..))",
        throwing = "exception")
    public void repositoryMethodThrew(JoinPoint joinPoint, Exception exception) {
        var signature = joinPoint.getSignature();
        LOGGER.warn("Repository method threw: {}::{} -> {}",
            signature.getDeclaringTypeName(),
            signature.getName(),
            exception.getClass().getName());
    }

    @AfterReturning(pointcut = "execution(public * com.daniilmiskevich.labs.*.cache.*.put*(..))")
    public void cachePut(JoinPoint joinPoint) {
        LOGGER.info("Cache entry added: {}", joinPoint.getSignature().getDeclaringTypeName());
    }

    @AfterReturning(
        pointcut = "execution(public * com.daniilmiskevich.labs.*.cache.*.get*(..))",
        returning = "result")
    public void cacheGot(JoinPoint joinPoint, Object result) {
        if (result == null || (result instanceof List && ((List<?>) result).isEmpty())) {
            LOGGER.info("Cache miss! {}", joinPoint.getSignature().getDeclaringTypeName());
        } else {
            LOGGER.info("Cache entry taken: {}", joinPoint.getSignature().getDeclaringTypeName());
        }

    }

}
