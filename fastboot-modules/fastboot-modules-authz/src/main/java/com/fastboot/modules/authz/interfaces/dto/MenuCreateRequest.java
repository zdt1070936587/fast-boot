package com.fastboot.modules.authz.interfaces.dto;

import jakarta.validation.constraints.NotBlank;

public record MenuCreateRequest(
    Long parentId, @NotBlank String name, String path, String component, String permCode) {}
