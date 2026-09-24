package com.fastboot.modules.authz.interfaces.dto;

/**
 * 菜单树节点展示对象
 *
 * @author zhaodt
 * @since 2026-09-19
 */
public record MenuView(
        /** 菜单 ID */ Long id,
        /** 父菜单 ID */ Long parentId,
        /** 菜单名称 */ String name,
        /** 路由路径 */ String path,
        /** 前端组件 */ String component,
        /** 权限编码 */ String permCode) {}
