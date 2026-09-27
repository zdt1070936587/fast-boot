package com.fastboot.modules.infra.application.storage;

import com.fastboot.common.error.BizException;
import com.fastboot.common.error.ErrorCode;
import com.fastboot.modules.infra.api.FileRef;
import com.fastboot.modules.infra.api.FileStoragePort;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 * 本地磁盘文件存储。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@Component
public class LocalFileStorage implements FileStoragePort {

    public static final String STORAGE = "LOCAL";

    @Override
    public FileRef upload(String name, String contentType, InputStream content, long sizeBytes) {
        throw new BizException(ErrorCode.INTERNAL_ERROR, "请通过 FileAppService 上传");
    }

    public FileRef upload(String name, String contentType, InputStream content, long sizeBytes, JsonNode config) {
        Path base = Path.of(config.path("basePath").asText("./data/files"));
        try {
            Files.createDirectories(base);
            String objectKey = UUID.randomUUID() + "-" + name;
            Path target = base.resolve(objectKey);
            Files.copy(content, target);
            String url = config.path("urlPrefix").asText("/files/") + objectKey;
            return new FileRef(null, name, objectKey, url, sizeBytes);
        } catch (IOException ex) {
            throw new BizException(ErrorCode.INTERNAL_ERROR, "文件写入失败");
        }
    }

    @Override
    public InputStream download(String path) {
        throw new BizException(ErrorCode.INTERNAL_ERROR, "请通过 FileAppService 下载");
    }

    public InputStream download(String path, JsonNode config) {
        Path base = Path.of(config.path("basePath").asText("./data/files"));
        try {
            return Files.newInputStream(base.resolve(path));
        } catch (IOException ex) {
            throw new BizException(ErrorCode.NOT_FOUND, "文件不存在");
        }
    }

    @Override
    public void delete(String path) {
        throw new BizException(ErrorCode.INTERNAL_ERROR, "请通过 FileAppService 删除");
    }

    public void delete(String path, JsonNode config) {
        Path base = Path.of(config.path("basePath").asText("./data/files"));
        try {
            Files.deleteIfExists(base.resolve(path));
        } catch (IOException ex) {
            throw new BizException(ErrorCode.INTERNAL_ERROR, "文件删除失败");
        }
    }

    @Override
    public String presign(String path, Duration ttl) {
        return path;
    }
}
