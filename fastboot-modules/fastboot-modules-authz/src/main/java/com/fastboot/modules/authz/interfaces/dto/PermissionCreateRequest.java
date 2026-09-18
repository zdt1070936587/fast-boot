package com.fastboot.modules.authz.interfaces.dto;

import jakarta.validation.constraints.NotBlank;

public record PermissionCreateRequest(
    @NotBlank String permType, @NotBlank String permCode, String name) {}
