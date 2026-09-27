package com.fastboot.ddd.infrastructure.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import java.time.OffsetDateTime;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

/**
 * MyBatis-Plus 自动填充处理器，维护 createdAt / updatedAt / createdBy / updatedBy。
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@Component
public class AuditMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        OffsetDateTime now = OffsetDateTime.now();
        strictInsertFill(metaObject, "createdAt", OffsetDateTime.class, now);
        strictInsertFill(metaObject, "updatedAt", OffsetDateTime.class, now);
        Long userId = currentUserId();
        if (userId != null) {
            strictInsertFill(metaObject, "createdBy", Long.class, userId);
            strictInsertFill(metaObject, "updatedBy", Long.class, userId);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        strictUpdateFill(metaObject, "updatedAt", OffsetDateTime.class, OffsetDateTime.now());
        Long userId = currentUserId();
        if (userId != null) {
            strictUpdateFill(metaObject, "updatedBy", Long.class, userId);
        }
    }

    private Long currentUserId() {
        try {
            Object loginId = cn.dev33.satoken.stp.StpUtil.getLoginIdDefaultNull();
            if (loginId instanceof Number n) {
                return n.longValue();
            }
            if (loginId != null) {
                return Long.parseLong(String.valueOf(loginId));
            }
        } catch (Throwable ignored) {
            // ignore
        }
        return null;
    }
}
