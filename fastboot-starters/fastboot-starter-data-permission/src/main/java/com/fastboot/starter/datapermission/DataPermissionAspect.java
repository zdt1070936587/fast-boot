package com.fastboot.starter.datapermission;

import cn.dev33.satoken.stp.StpUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;

/**
 * 将 {@link DataPermission} 注解写入请求级上下文。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@Aspect
@Order(60)
public class DataPermissionAspect {

    private final DataPermissionProperties properties;

    public DataPermissionAspect(DataPermissionProperties properties) {
        this.properties = properties;
    }

    @Around("@annotation(com.fastboot.starter.datapermission.DataPermission) || @within(com.fastboot.starter.datapermission.DataPermission)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        DataPermission annotation =
                AnnotationUtils.findAnnotation(signature.getMethod(), DataPermission.class);
        if (annotation == null) {
            annotation =
                    AnnotationUtils.findAnnotation(signature.getDeclaringType(), DataPermission.class);
        }
        boolean enabled = properties.isEnabled() && (annotation == null || annotation.enabled());
        Long userId = null;
        Long selfOrgId = null;
        try {
            if (StpUtil.isLogin()) {
                userId = StpUtil.getLoginIdAsLong();
                Object org = StpUtil.getSession().get("orgUnitId");
                if (org instanceof Number n) {
                    selfOrgId = n.longValue();
                }
            }
        } catch (Throwable ignored) {
            // ignore
        }
        DataPermissionContext previous = DataPermissionContext.get();
        try {
            DataPermissionContext.set(new DataPermissionContext(userId, selfOrgId, enabled));
            return pjp.proceed();
        } finally {
            if (previous == null) {
                DataPermissionContext.clear();
            } else {
                DataPermissionContext.set(previous);
            }
        }
    }
}
