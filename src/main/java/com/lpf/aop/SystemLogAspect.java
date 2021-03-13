package com.lpf.aop;

import com.lpf.core.vo.CommonResult;
import com.lpf.model.SystemLog;
import com.lpf.service.SystemLogService;
import com.lpf.util.SnowflakeIdWorker;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 日志切入类
 */
@Aspect
@Component
@Order(-5)
public class SystemLogAspect {

    private static Logger log = LoggerFactory.getLogger(SystemLogAspect.class);

    private static SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(25,25);

    @Resource
    private SystemLogService systemLogService;

    /**
     * 定义service切入点拦截规则，拦截SystemServiceLog注解方法
     */
    @Pointcut("@annotation(com.lpf.aop.SystemServiceLog)")
    public void serviceAspect(){}

    /**
     * 定义controller切入点拦截规则，拦截SystemControllerLog注解方法
     */
    @Pointcut("@annotation(com.lpf.aop.SystemControllerLog)")
    public void controllerAspect(){}

    @Around("controllerAspect()")
    public CommonResult recordLog(ProceedingJoinPoint joinPoint) throws Throwable {
        SystemLog systemLog = new SystemLog();
        systemLog.setLogId(snowflakeIdWorker.nextId());
        Object proceed = null;

        //获取session的用户
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        request.getSession().getAttribute("user");

        String ip = getRemoteHost(request);
        systemLog.setLogIp(ip);

        systemLog.setActionMethod(request.getRequestURI());

        systemLog.setAddTime(new Date());

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        SystemControllerLog systemControllerLog = method.getAnnotation(SystemControllerLog.class);

        systemLog.setLogName(systemControllerLog.description());

        proceed = joinPoint.proceed();
        CommonResult commonResult = (CommonResult) proceed;

        systemLog.setActionResult(commonResult.getMessage());

        Object[] params = joinPoint.getArgs();
        String paramStr = "";
        for (Object param: params) {
            paramStr += param;
        }
        systemLog.setContent(paramStr);

        //插入日志
        systemLogService.insertSystemLog(systemLog);

        return commonResult;

    }

    /**
     * 获取目标主机的ip
     * @param request
     * @return
     */
    private String getRemoteHost(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip.contains("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
    }

    public Map<String, String> converMap(Map<String, String[]> paramMap) {
        Map<String, String> map = new HashMap<>();
        for (String key:
             paramMap.keySet()) {
            map.put(key, paramMap.get(key)[0]);
        }
        return map;
    }

}
