package com.fastboot.modules.authz.api;

import java.util.List;

/**
 * 用户权限与角色查询端口（供其他模块调用）
 *
 * @author zhaodt
 * @since 2026-09-19
 */
public interface PermissionQueryPort {
    List<String> listByUserId(Long userId);

    List<String> listRolesByUserId(Long userId);

    void evict(Long userId);
}
