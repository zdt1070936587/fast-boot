package com.fastboot.modules.org.interfaces.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 创建组织节点请求体
 *
 * @author zhaodt
 * @since 2026-09-19
 */
public record OrgCreateRequest(
    /** 上级组织 ID，根节点可为空 */ Long parentId, /** 组织名称 */ @NotBlank String name) {}
