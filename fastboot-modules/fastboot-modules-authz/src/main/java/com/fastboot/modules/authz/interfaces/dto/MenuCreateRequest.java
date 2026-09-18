package com.fastboot.modules.authz.interfaces.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 创建菜单请求体
 *
 * @author zhaodt
 * @since 2026-09-19
 */
public record MenuCreateRequest(
    /** 父菜单 ID，根节点可为空 */ Long parentId,
    /** 菜单名称 */ @NotBlank String name,
    /** 前端路由路径 */ String path,
    /** 前端组件路径 */ String component,
    /** 关联权限编码 */ String permCode) {}
