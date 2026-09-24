package com.fastboot.ddd.infrastructure.aop;

import com.fastboot.common.audit.OperationLog;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * {@link OperationLog} 注解切面，记录操作审计日志。
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
        log.info("audit action={} method={} args={}",
                operationLog.value(),
                joinPoint.getSignature().toShortString(),
                joinPoint.getArgs());
    }
}
