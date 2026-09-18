package com.fastboot.modules.authz.api;

import java.util.List;

public record PermissionSnapshot(Long userId, List<String> permissions, List<String> roles) {}
