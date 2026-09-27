package com.fastboot.starter.tenant;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

/**
 * {@link TenantIgnore} 切面：方法执行期间关闭租户条件。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@Aspect
@Order(50)
public class TenantIgnoreAspect {

    @Around("@annotation(com.fastboot.starter.tenant.TenantIgnore) || @within(com.fastboot.starter.tenant.TenantIgnore)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        return TenantContext.ignore(
                () -> {
                    try {
                        return pjp.proceed();
                    } catch (Throwable e) {
                        if (e instanceof RuntimeException re) {
                            throw re;
                        }
                        if (e instanceof Error err) {
                            throw err;
                        }
                        throw new RuntimeException(e);
                    }
                });
    }
}
