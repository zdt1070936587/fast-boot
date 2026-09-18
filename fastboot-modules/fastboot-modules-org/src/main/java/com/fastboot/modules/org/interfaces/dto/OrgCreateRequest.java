package com.fastboot.modules.org.interfaces.dto;

import jakarta.validation.constraints.NotBlank;

public record OrgCreateRequest(Long parentId, @NotBlank String name) {}
