package com.fastboot.ddd.infrastructure.aop;

import com.fastboot.common.api.TraceIdHolder;
import com.fastboot.common.audit.OperationLog;
import com.fastboot.common.audit.OperationLogEvent;
import com.fastboot.common.error.BizException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * {@link OperationLog} 切面：发布 {@link OperationLogEvent} 供落库。
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@Aspect
@Component
public class OperationLogAspect {

    private final ApplicationEventPublisher publisher;
    private final ObjectMapper objectMapper;

    public OperationLogAspect(ApplicationEventPublisher publisher, ObjectMapper objectMapper) {
        this.publisher = publisher;
        this.objectMapper = objectMapper;
    }

    @Around("@annotation(operationLog)")
    public Object around(ProceedingJoinPoint joinPoint, OperationLog operationLog) throws Throwable {
        long start = System.currentTimeMillis();
        boolean success = true;
        Integer errorCode = null;
        Object result = null;
        try {
            result = joinPoint.proceed();
            return result;
        } catch (Throwable ex) {
            success = false;
            if (ex instanceof BizException biz) {
                errorCode = biz.getCode();
            } else {
                errorCode = 500000;
            }
            throw ex;
        } finally {
            long duration = System.currentTimeMillis() - start;
            publisher.publishEvent(
                    new OperationLogEvent(
                            currentTenantId(),
                            currentUserId(),
                            operationLog.value(),
                            joinPoint.getSignature().toShortString(),
                            toJson(SpelSupport.evalAll(joinPoint, operationLog.args())),
                            success ? toJson(result) : null,
                            duration,
                            TraceIdHolder.current(),
                            clientIp(),
                            userAgent(),
                            success,
                            errorCode));
        }
    }

    private Long currentUserId() {
        try {
            Object loginId = cn.dev33.satoken.stp.StpUtil.getLoginIdDefaultNull();
            if (loginId instanceof Number n) {
                return n.longValue();
            }
        } catch (Throwable ignored) {
            // ignore
        }
        return null;
    }

    private Long currentTenantId() {
        try {
            Class<?> ctx = Class.forName("com.fastboot.starter.tenant.TenantContext");
            Object value = ctx.getMethod("get").invoke(null);
            if (value instanceof Number n) {
                return n.longValue();
            }
        } catch (Throwable ignored) {
            // tenant starter 可选
        }
        return 1L;
    }

    private String clientIp() {
        try {
            ServletRequestAttributes attrs =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                return attrs.getRequest().getRemoteAddr();
            }
        } catch (Throwable ignored) {
            // ignore
        }
        return null;
    }

    private String userAgent() {
        try {
            ServletRequestAttributes attrs =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                return attrs.getRequest().getHeader("User-Agent");
            }
        } catch (Throwable ignored) {
            // ignore
        }
        return null;
    }

    private String toJson(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception ex) {
            return String.valueOf(value);
        }
    }
}
