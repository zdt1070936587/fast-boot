package com.fastboot.modules.authz.api;

/**
 * 菜单快照（跨模块传输）。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
public record MenuSnapshot(
        Long id, Long parentId, String name, String path, String component, String permCode) {}
