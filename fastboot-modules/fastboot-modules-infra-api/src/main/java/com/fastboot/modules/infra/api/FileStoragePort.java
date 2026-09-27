package com.fastboot.modules.infra.api;

import java.io.InputStream;
import java.time.Duration;

/**
 * 文件存储端口。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
public interface FileStoragePort {

    FileRef upload(String name, String contentType, InputStream content, long sizeBytes);

    InputStream download(String path);

    void delete(String path);

    String presign(String path, Duration ttl);
}
