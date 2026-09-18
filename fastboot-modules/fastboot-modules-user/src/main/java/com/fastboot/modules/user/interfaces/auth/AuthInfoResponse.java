package com.fastboot.modules.user.interfaces.auth;

public record AuthInfoResponse(Long userId, String username, String displayName, Long orgUnitId) {}
