package com.fastboot.modules.authz.interfaces.dto;

import jakarta.validation.constraints.NotBlank;

public record RoleCreateRequest(@NotBlank String code, @NotBlank String name) {}
