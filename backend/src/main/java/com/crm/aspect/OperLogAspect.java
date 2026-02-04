package com.crm.aspect;

import com.crm.entity.sys.SysOperLog;
import com.crm.service.sys.SysOperLogService;
import com.crm.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.UUID;

/**
 * 操作日志记录切面
 */
@Slf4j
@Aspect
@Component
public class OperLogAspect {

    @Autowired
    private SysOperLogService sysOperLogService;

    /**
     * 定义切点
     */
    @Pointcut("execution(* com.crm.controller..*.*(..))")
    public void operLogPointCut() {
        log.info("OperLogPointCut initialized");
    }

    /**
     * 操作成功后记录日志
     */
    @AfterReturning(pointcut = "operLogPointCut()", returning = "result")
    public void saveOperLog(JoinPoint joinPoint, Object result) {
        try {
            log.info("开始记录操作日志");
            
            // 获取当前请求
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            if (request == null) {
                log.error("获取请求对象失败");
                return;
            }

            // 创建操作日志
            SysOperLog operLog = new SysOperLog();

            // 设置操作时间
            operLog.setOperTime(new Date());

            // 设置操作人员
            try {
                operLog.setOperName(SecurityUtils.getUsername());
            } catch (Exception e) {
                operLog.setOperName("匿名用户");
                log.warn("获取用户名失败: {}", e.getMessage());
            }

            // 设置操作URL
            operLog.setOperUrl(request.getRequestURL().toString());

            // 设置操作IP
            operLog.setOperIp(request.getRemoteAddr());

            // 设置请求方式
            operLog.setRequestMethod(request.getMethod());

            // 设置操作方法
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            operLog.setMethod(className + "." + methodName);

            // 设置操作标题（简单处理，使用类名和方法名）
            operLog.setTitle(className.substring(className.lastIndexOf(".") + 1) + " - " + methodName);

            // 设置业务类型（默认为0-其他）
            operLog.setBusinessType(0);

            // 设置操作类型（默认为1-后台用户）
            operLog.setOperatorType(1);

            // 设置操作状态（1-成功）
            operLog.setStatus(1);

            // 保存操作日志
            boolean saved = sysOperLogService.saveLog(operLog);
            if (saved) {
                log.info("操作日志记录成功: {}, URL: {}", operLog.getTitle(), operLog.getOperUrl());
            } else {
                log.error("操作日志保存失败");
            }

        } catch (Exception e) {
            log.error("操作日志记录失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 操作失败后记录日志
     */
    @AfterThrowing(pointcut = "operLogPointCut()", throwing = "e")
    public void saveOperLogError(JoinPoint joinPoint, Exception e) {
        try {
            // 获取当前请求
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

            // 创建操作日志
            SysOperLog operLog = new SysOperLog();

            // 设置操作时间
            operLog.setOperTime(new Date());

            // 设置操作人员
            try {
                operLog.setOperName(SecurityUtils.getUsername());
            } catch (Exception ex) {
                operLog.setOperName("匿名用户");
            }

            // 设置操作URL
            operLog.setOperUrl(request.getRequestURL().toString());

            // 设置操作IP
            operLog.setOperIp(request.getRemoteAddr());

            // 设置请求方式
            operLog.setRequestMethod(request.getMethod());

            // 设置操作方法
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            operLog.setMethod(className + "." + methodName);

            // 设置操作标题（简单处理，使用类名和方法名）
            operLog.setTitle(className.substring(className.lastIndexOf(".") + 1) + " - " + methodName);

            // 设置业务类型（默认为0-其他）
            operLog.setBusinessType(0);

            // 设置操作类型（默认为1-后台用户）
            operLog.setOperatorType(1);

            // 设置操作状态（0-失败）
            operLog.setStatus(0);

            // 设置错误信息
            operLog.setErrorMsg(e.getMessage());

            // 保存操作日志
            sysOperLogService.saveLog(operLog);
            log.info("操作日志记录成功: {}", operLog.getTitle());

        } catch (Exception ex) {
            log.error("操作日志记录失败: {}", ex.getMessage());
        }
    }
}
