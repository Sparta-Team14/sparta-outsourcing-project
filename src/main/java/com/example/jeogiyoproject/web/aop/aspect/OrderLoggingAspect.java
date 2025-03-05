package com.example.jeogiyoproject.web.aop.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class OrderLoggingAspect {

    private final ObjectMapper objectMapper;

    @Pointcut("@annotation(com.example.jeogiyoproject.web.aop.annotation.OrderLogging)")
    public void orderLoggingPoint() {
    }

    @AfterReturning(pointcut = "orderLoggingPoint()", returning = "result")
    public void logOrderStatusChange(Object result) throws Throwable {
        String responseBody = objectMapper.writeValueAsString(result);
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(responseBody);
        log.info("API Response: {}", jsonObject.get("body"));
    }
}
