package com.fastboot.modules.system.api;

import java.util.Optional;

/**
 * 系统配置端口。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
public interface ConfigPort {

    Optional<String> get(String key);

    void set(String key, String value);
}
