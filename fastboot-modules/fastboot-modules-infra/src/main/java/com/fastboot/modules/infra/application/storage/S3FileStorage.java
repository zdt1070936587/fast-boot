package com.fastboot.modules.infra.application.storage;

import com.fastboot.common.error.BizException;
import com.fastboot.common.error.ErrorCode;
import com.fastboot.modules.infra.api.FileRef;
import com.fastboot.modules.infra.api.FileStoragePort;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.InputStream;
import java.time.Duration;
import java.util.UUID;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

/**
 * S3 兼容对象存储。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@Component
public class S3FileStorage implements FileStoragePort {

    public static final String STORAGE = "S3";

    @Override
    public FileRef upload(String name, String contentType, InputStream content, long sizeBytes) {
        throw new BizException(ErrorCode.INTERNAL_ERROR, "请通过 FileAppService 上传");
    }

    public FileRef upload(String name, String contentType, InputStream content, long sizeBytes, JsonNode config) {
        String bucket = config.path("bucket").asText();
        String key = UUID.randomUUID() + "/" + name;
        try (S3Client client = client(config)) {
            client.putObject(
                    PutObjectRequest.builder().bucket(bucket).key(key).contentType(contentType).build(),
                    RequestBody.fromInputStream(content, sizeBytes));
        }
        String url = config.path("publicUrl").asText("") + key;
        return new FileRef(null, name, key, url, sizeBytes);
    }

    @Override
    public InputStream download(String path) {
        throw new BizException(ErrorCode.INTERNAL_ERROR, "请通过 FileAppService 下载");
    }

    public InputStream download(String path, JsonNode config) {
        String bucket = config.path("bucket").asText();
        try (S3Client client = client(config)) {
            return client.getObject(GetObjectRequest.builder().bucket(bucket).key(path).build());
        }
    }

    @Override
    public void delete(String path) {
        throw new BizException(ErrorCode.INTERNAL_ERROR, "请通过 FileAppService 删除");
    }

    public void delete(String path, JsonNode config) {
        String bucket = config.path("bucket").asText();
        try (S3Client client = client(config)) {
            client.deleteObject(DeleteObjectRequest.builder().bucket(bucket).key(path).build());
        }
    }

    @Override
    public String presign(String path, Duration ttl) {
        throw new BizException(ErrorCode.INTERNAL_ERROR, "请通过 FileAppService 预签名");
    }

    public String presign(String path, Duration ttl, JsonNode config) {
        String bucket = config.path("bucket").asText();
        try (S3Presigner presigner = presigner(config)) {
            var presign =
                    presigner.presignGetObject(
                            GetObjectPresignRequest.builder()
                                    .signatureDuration(ttl)
                                    .getObjectRequest(b -> b.bucket(bucket).key(path))
                                    .build());
            return presign.url().toString();
        }
    }

    private S3Client client(JsonNode config) {
        return S3Client.builder()
                .region(Region.of(config.path("region").asText("us-east-1")))
                .credentialsProvider(credentials(config))
                .build();
    }

    private S3Presigner presigner(JsonNode config) {
        return S3Presigner.builder()
                .region(Region.of(config.path("region").asText("us-east-1")))
                .credentialsProvider(credentials(config))
                .build();
    }

    private StaticCredentialsProvider credentials(JsonNode config) {
        return StaticCredentialsProvider.create(
                AwsBasicCredentials.create(
                        config.path("accessKey").asText(),
                        config.path("secretKey").asText()));
    }
}
