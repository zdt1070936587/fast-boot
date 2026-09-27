package com.fastboot.modules.infra.api;

/**
 * 文件存储结果引用。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
public record FileRef(Long id, String name, String path, String url, long sizeBytes) {}
