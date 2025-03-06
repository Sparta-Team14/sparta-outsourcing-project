package com.example.jeogiyoproject.web.aop.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class TimerLoggingAspect {

    @Pointcut("@annotation(com.example.jeogiyoproject.web.aop.annotation.Timer)")
    private void LoggingPoint(){

    }
    @Around("LoggingPoint()")
    public Object around(ProceedingJoinPoint joinPoint)throws Throwable{
        long requestTime = System.currentTimeMillis();
        log.info("[AOP API 요청 시각] requestTime = {}",requestTime);
        Object result = joinPoint.proceed();
        long responseTime = System.currentTimeMillis();
        log.info("[AOP API 응답 시간] responseTime = {}",responseTime);
        log.info("[AOP API 소요 시간] elapsedTime = {}ms",responseTime-requestTime);
        return result;
    }
}
