package com.daniilmiskevich.labs.dev.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
public class VisitsCounterAspect {

    private final ConcurrentHashMap<String, Long> visits = new ConcurrentHashMap<>();

    public Map<String, Long> getVisitsCounts() {
        return Collections.unmodifiableMap(visits);
    }

    @Before("execution(public * com.daniilmiskevich.labs.*.controller.*.*(..))")
    public void controllerMethodAccessed(JoinPoint joinPoint) {
        var signature = joinPoint.getSignature();
        visits.compute(
            signature.getDeclaringTypeName() + "::" + signature.getName(),
            (methodName, visitsCount) -> Optional.ofNullable(visitsCount).orElse(0L) + 1);
    }
}
