package com.fastboot.modules.authz.interfaces.dto;

public record MenuView(
    Long id, Long parentId, String name, String path, String component, String permCode) {}
