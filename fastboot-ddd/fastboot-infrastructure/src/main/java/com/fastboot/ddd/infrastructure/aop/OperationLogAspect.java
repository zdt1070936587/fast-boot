package com.fastboot.ddd.infrastructure.aop;

import com.fastboot.common.api.TraceIdHolder;
import com.fastboot.common.audit.OperationLog;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * {@link OperationLog} 注解切面，记录操作审计日志（仅记录显式声明的 SpEL 参数）。
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@Aspect
@Component
public class OperationLogAspect {

    /** 审计日志记录器 */
    private static final Logger log = LoggerFactory.getLogger(OperationLogAspect.class);

    /** 方法正常返回后写入审计日志 */
    @AfterReturning(pointcut = "@annotation(operationLog)", returning = "result")
    public void after(JoinPoint joinPoint, OperationLog operationLog, Object result) {
        Object loginId = null;
        try {
            loginId = cn.dev33.satoken.stp.StpUtil.getLoginIdDefaultNull();
        } catch (Throwable ignored) {
            // Sa-Token 未初始化或非 Web 上下文时忽略
        }
        log.info(
                "audit action={} method={} loginId={} traceId={} args={}",
                operationLog.value(),
                joinPoint.getSignature().toShortString(),
                loginId,
                TraceIdHolder.current(),
                SpelSupport.evalAll(joinPoint, operationLog.args()));
    }
}
