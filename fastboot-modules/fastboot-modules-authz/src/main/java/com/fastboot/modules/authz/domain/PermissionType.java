package com.fastboot.modules.authz.domain;

/**
 * RBAC 权限类型枚举
 *
 * @author zhaodt
 * @since 2026-09-19
 */
public enum PermissionType {
    /** 菜单权限 */
    MENU,
    /** 按钮权限 */
    BUTTON,
    /** 接口权限 */
    API,
    /** 数据权限 */
    DATA
}
