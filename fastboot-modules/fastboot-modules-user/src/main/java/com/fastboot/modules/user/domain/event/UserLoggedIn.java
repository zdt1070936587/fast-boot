package com.fastboot.modules.user.domain.event;

public record UserLoggedIn(Long userId, String username) {}
