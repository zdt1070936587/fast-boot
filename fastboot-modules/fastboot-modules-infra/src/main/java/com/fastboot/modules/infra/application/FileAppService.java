package com.fastboot.modules.infra.application;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fastboot.common.error.BizException;
import com.fastboot.common.error.ErrorCode;
import com.fastboot.modules.infra.api.FileRef;
import com.fastboot.modules.infra.api.FileStoragePort;
import com.fastboot.modules.infra.application.storage.LocalFileStorage;
import com.fastboot.modules.infra.application.storage.S3FileStorage;
import com.fastboot.modules.infra.infrastructure.mapper.SysFileConfigMapper;
import com.fastboot.modules.infra.infrastructure.mapper.SysFileMapper;
import com.fastboot.modules.infra.infrastructure.persistence.entity.SysFileConfigEntity;
import com.fastboot.modules.infra.infrastructure.persistence.entity.SysFileEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.time.Duration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 文件上传/下载应用服务（按 master 配置路由存储后端）。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@Service
public class FileAppService implements FileStoragePort {

    private final SysFileConfigMapper fileConfigMapper;
    private final SysFileMapper fileMapper;
    private final LocalFileStorage localFileStorage;
    private final S3FileStorage s3FileStorage;
    private final ObjectMapper objectMapper;

    public FileAppService(
            SysFileConfigMapper fileConfigMapper,
            SysFileMapper fileMapper,
            LocalFileStorage localFileStorage,
            S3FileStorage s3FileStorage,
            ObjectMapper objectMapper) {
        this.fileConfigMapper = fileConfigMapper;
        this.fileMapper = fileMapper;
        this.localFileStorage = localFileStorage;
        this.s3FileStorage = s3FileStorage;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public FileRef upload(String name, String contentType, InputStream content, long sizeBytes) {
        SysFileConfigEntity config = masterConfig();
        JsonNode json = parseConfig(config.getConfig());
        FileRef ref;
        if (S3FileStorage.STORAGE.equalsIgnoreCase(config.getStorage())) {
            ref = s3FileStorage.upload(name, contentType, content, sizeBytes, json);
        } else {
            ref = localFileStorage.upload(name, contentType, content, sizeBytes, json);
        }
        SysFileEntity entity = new SysFileEntity();
        entity.setConfigId(config.getId());
        entity.setName(name);
        entity.setPath(ref.path());
        entity.setUrl(ref.url());
        entity.setSizeBytes(sizeBytes);
        entity.setContentType(contentType);
        fileMapper.insert(entity);
        return new FileRef(entity.getId(), ref.name(), ref.path(), ref.url(), ref.sizeBytes());
    }

    /** 文件列表（按创建时间倒序，最多 200 条） */
    @Transactional(readOnly = true)
    public java.util.List<SysFileEntity> list(String keyword) {
        var query = Wrappers.<SysFileEntity>lambdaQuery();
        if (keyword != null && !keyword.isBlank()) {
            query.like(SysFileEntity::getName, keyword);
        }
        query.orderByDesc(SysFileEntity::getId).last("limit 200");
        return fileMapper.selectList(query);
    }

    @Override
    @Transactional(readOnly = true)
    public InputStream download(String path) {
        SysFileEntity file =
                fileMapper.selectOne(
                        Wrappers.<SysFileEntity>lambdaQuery().eq(SysFileEntity::getPath, path));
        if (file == null) {
            throw new BizException(ErrorCode.NOT_FOUND);
        }
        SysFileConfigEntity config = fileConfigMapper.selectById(file.getConfigId());
        JsonNode json = parseConfig(config.getConfig());
        if (S3FileStorage.STORAGE.equalsIgnoreCase(config.getStorage())) {
            return s3FileStorage.download(path, json);
        }
        return localFileStorage.download(path, json);
    }

    @Override
    @Transactional
    public void delete(String path) {
        SysFileEntity file =
                fileMapper.selectOne(
                        Wrappers.<SysFileEntity>lambdaQuery().eq(SysFileEntity::getPath, path));
        if (file == null) {
            return;
        }
        SysFileConfigEntity config = fileConfigMapper.selectById(file.getConfigId());
        JsonNode json = parseConfig(config.getConfig());
        if (S3FileStorage.STORAGE.equalsIgnoreCase(config.getStorage())) {
            s3FileStorage.delete(path, json);
        } else {
            localFileStorage.delete(path, json);
        }
        fileMapper.deleteById(file.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public String presign(String path, Duration ttl) {
        SysFileEntity file =
                fileMapper.selectOne(
                        Wrappers.<SysFileEntity>lambdaQuery().eq(SysFileEntity::getPath, path));
        if (file == null) {
            throw new BizException(ErrorCode.NOT_FOUND);
        }
        SysFileConfigEntity config = fileConfigMapper.selectById(file.getConfigId());
        JsonNode json = parseConfig(config.getConfig());
        if (S3FileStorage.STORAGE.equalsIgnoreCase(config.getStorage())) {
            return s3FileStorage.presign(path, ttl, json);
        }
        return localFileStorage.presign(path, ttl);
    }

    private SysFileConfigEntity masterConfig() {
        SysFileConfigEntity config =
                fileConfigMapper.selectOne(
                        Wrappers.<SysFileConfigEntity>lambdaQuery().eq(SysFileConfigEntity::getMaster, true));
        if (config == null) {
            throw new BizException(ErrorCode.NOT_FOUND, "未配置文件存储");
        }
        return config;
    }

    private JsonNode parseConfig(String raw) {
        try {
            if (raw == null || raw.isBlank()) {
                return objectMapper.createObjectNode();
            }
            return objectMapper.readTree(raw);
        } catch (Exception ex) {
            throw new BizException(ErrorCode.VALIDATION_FAILED, "存储配置 JSON 无效");
        }
    }
}
