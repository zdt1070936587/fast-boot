package com.fastboot.modules.authz.api;

import java.util.List;

/**
 * 数据范围查询端口：解析用户可见的组织 ID 列表。
 *
 * @author zhaodt
 * @since 2026-09-24
 */
public interface DataScopePort {
    /**
     * 空列表表示全部组织可见（无行级限制）；非空表示仅可见列出的组织 ID。
     */
    List<Long> visibleOrgIds(Long userId, Long selfOrgId);
}
