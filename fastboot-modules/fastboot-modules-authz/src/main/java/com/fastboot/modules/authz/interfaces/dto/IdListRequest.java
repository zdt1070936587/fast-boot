package com.fastboot.modules.authz.interfaces.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record IdListRequest(@NotNull List<Long> ids) {}
