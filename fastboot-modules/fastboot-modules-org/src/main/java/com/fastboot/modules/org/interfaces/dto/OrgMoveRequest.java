package com.fastboot.modules.org.interfaces.dto;

import jakarta.validation.constraints.NotNull;

public record OrgMoveRequest(@NotNull Long parentId) {}
