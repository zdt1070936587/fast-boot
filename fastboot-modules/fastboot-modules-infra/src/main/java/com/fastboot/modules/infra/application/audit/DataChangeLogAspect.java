package com.fastboot.modules.infra.application.audit;

import cn.dev33.satoken.stp.StpUtil;
import com.fastboot.ddd.infrastructure.aop.SpelSupport;
import com.fastboot.modules.infra.infrastructure.mapper.SysDataChangeLogMapper;
import com.fastboot.modules.infra.infrastructure.persistence.entity.SysDataChangeLogEntity;
import com.fastboot.starter.tenant.TenantContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 数据变更审计切面。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@Aspect
@Component
public class DataChangeLogAspect {

    private final SysDataChangeLogMapper dataChangeLogMapper;
    private final ObjectMapper objectMapper;

    public DataChangeLogAspect(SysDataChangeLogMapper dataChangeLogMapper, ObjectMapper objectMapper) {
        this.dataChangeLogMapper = dataChangeLogMapper;
        this.objectMapper = objectMapper;
    }

    @Around("@annotation(dataChangeLog)")
    public Object around(ProceedingJoinPoint joinPoint, DataChangeLog dataChangeLog) throws Throwable {
        Object before = joinPoint.getArgs().length > 0 ? joinPoint.getArgs()[0] : null;
        Object result = joinPoint.proceed();
        SysDataChangeLogEntity entity = new SysDataChangeLogEntity();
        entity.setTenantId(TenantContext.get() != null ? TenantContext.get() : 1L);
        entity.setAggregateType(dataChangeLog.aggregateType());
        entity.setAggregateId(SpelSupport.eval(joinPoint, dataChangeLog.aggregateId()));
        entity.setBeforeJson(toJson(before));
        entity.setAfterJson(toJson(result != null ? result : before));
        entity.setCreatedBy(currentUserId());
        dataChangeLogMapper.insert(entity);
        return result;
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

    private Long currentUserId() {
        try {
            Object loginId = StpUtil.getLoginIdDefaultNull();
            if (loginId instanceof Number number) {
                return number.longValue();
            }
        } catch (Throwable ignored) {
            // ignore
        }
        return null;
    }
}
