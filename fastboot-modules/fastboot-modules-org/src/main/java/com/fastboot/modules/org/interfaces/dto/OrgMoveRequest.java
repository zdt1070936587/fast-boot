package com.fastboot.modules.org.interfaces.dto;

import jakarta.validation.constraints.NotNull;

/**
 * 组织节点移动请求体
 *
 * @author zhaodt
 * @since 2026-09-19
 */
public record OrgMoveRequest(/** 新的上级组织 ID */ @NotNull Long parentId) {}
