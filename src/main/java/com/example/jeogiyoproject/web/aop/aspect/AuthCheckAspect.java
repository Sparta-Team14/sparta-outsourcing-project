package com.example.jeogiyoproject.web.aop.aspect;

import com.example.jeogiyoproject.global.exception.CustomException;
import com.example.jeogiyoproject.global.exception.ErrorCode;
import com.example.jeogiyoproject.web.aop.annotation.AuthCheck;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@Aspect
@Slf4j
@RequiredArgsConstructor
public class AuthCheckAspect {

    @Pointcut("@annotation(com.example.jeogiyoproject.web.aop.annotation.AuthCheck)")
    private void AuthCheckPoint(){

    }

    @Around("AuthCheckPoint() && @annotation(authCheck)")
    public Object checkUserRole(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String userRole = (String) request.getAttribute("userRole");
        log.info("role = {}", userRole);

        if (userRole == null || !userRole.equals(authCheck.value().name())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        return joinPoint.proceed();
    }
}
