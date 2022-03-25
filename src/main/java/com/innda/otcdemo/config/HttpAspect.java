package com.innda.otcdemo.config;

import com.alibaba.fastjson.JSON;
import com.innda.otcdemo.common.exception.BusinessException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 日志
 *
 * @author fugq
 */
@Aspect
@Component
@Slf4j
@Data
public class HttpAspect {
    private long time;
    private Map<String, Object> requestMap = new HashMap<>();
    private boolean isClear;

    //实验acess key

    //测试sourcetree

    @Pointcut("execution(public *  cn.iinda.otc.controller.*.*(..))")
    public void log() {
    }

    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        time = System.currentTimeMillis();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new BusinessException("attributes为空");
        }
        HttpServletRequest request = attributes.getRequest();
        //url
        requestMap.put("requestUrl", request.getRequestURL());
        //method
        requestMap.put("method", request.getMethod());
        //ip
        requestMap.put("ip", request.getRemoteAddr());
        //类方法
        requestMap.put("classMethodName", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        Object[] args = joinPoint.getArgs();
        String params = "";
        log.debug("传入参数request：{}", args);
        for (Object objects : args) {
            params += objects + ",";
        }
        if (!params.equals("")) {
            params = params.substring(0, params.length() - 1);
            params = "(" + params + ")";
        }
        requestMap.put("params", params);
    }

    @After("log()")
    public void doAfter() {
        requestMap.put("elapsedTime", System.currentTimeMillis() - time);
        log.info("request:{}", JSON.toJSONString(requestMap));
    }

    @AfterReturning(returning = "object", pointcut = "log()")
    public void doAfterReturning(Object object) {
        if (object != null) {
            log.info("response:{}", JSON.toJSONString(object));
        }
    }
}
