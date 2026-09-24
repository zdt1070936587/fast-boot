package com.fastboot.modules.authz.api;

import java.util.List;

/**
 * 用户数据范围快照（内部服务传输）。
 *
 * @param userId 用户 ID
 * @param visibleOrgIds 可见组织 ID；空列表表示全部可见
 * @author zhaodt
 * @since 2026-09-24
 */
public record DataScopeSnapshot(Long userId, List<Long> visibleOrgIds) {}
