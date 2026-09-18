package com.fastboot.ddd.infrastructure.aop;

import com.fastboot.common.audit.OperationLog;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class OperationLogAspect {

  private static final Logger log = LoggerFactory.getLogger(OperationLogAspect.class);

  @AfterReturning(pointcut = "@annotation(operationLog)", returning = "result")
  public void after(JoinPoint joinPoint, OperationLog operationLog, Object result) {
    log.info("audit action={} method={} args={}",
        operationLog.value(),
        joinPoint.getSignature().toShortString(),
        joinPoint.getArgs());
  }
}
