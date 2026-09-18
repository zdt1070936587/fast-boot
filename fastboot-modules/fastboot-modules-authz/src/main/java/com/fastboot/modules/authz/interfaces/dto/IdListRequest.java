package com.fastboot.modules.authz.interfaces.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 批量 ID 列表请求体（用于角色/权限/菜单绑定）
 *
 * @author zhaodt
 * @since 2026-09-19
 */
public record IdListRequest(/** 主键 ID 列表 */ @NotNull List<Long> ids) {}
