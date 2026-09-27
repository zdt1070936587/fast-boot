package com.fastboot.modules.org.interfaces.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 组织重命名请求。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
public record OrgUpdateRequest(@NotBlank String name) {}
