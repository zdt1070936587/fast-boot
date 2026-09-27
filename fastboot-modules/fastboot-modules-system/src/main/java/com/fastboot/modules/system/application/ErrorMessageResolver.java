package com.fastboot.modules.system.application;

import com.fastboot.common.error.ErrorCode;
import com.fastboot.starter.tenant.TenantKeyPrefix;
import java.time.Duration;
import java.util.Locale;
import java.util.Optional;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 * 错误消息解析：数据库覆盖 &gt; MessageSource &gt; 枚举默认。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@Component
public class ErrorMessageResolver {

    private static final Duration TTL = Duration.ofHours(1);

    private final ErrorCodeAppService errorCodeAppService;
    private final MessageSource messageSource;
    private final RedissonClient redissonClient;

    public ErrorMessageResolver(
            ErrorCodeAppService errorCodeAppService,
            MessageSource messageSource,
            RedissonClient redissonClient) {
        this.errorCodeAppService = errorCodeAppService;
        this.messageSource = messageSource;
        this.redissonClient = redissonClient;
    }

    public String resolve(int code, Locale locale) {
        String loc = locale == null ? "zh_CN" : locale.toString();
        String key = TenantKeyPrefix.of("fb:errmsg:" + code + ":" + loc);
        RBucket<String> bucket = redissonClient.getBucket(key);
        String cached = bucket.get();
        if (cached != null) {
            return cached;
        }
        var entity = errorCodeAppService.findByCodeAndLocale(code, loc);
        if (entity != null && entity.getMessage() != null) {
            bucket.set(entity.getMessage(), TTL);
            return entity.getMessage();
        }
        Optional<ErrorCode> enumCode =
                java.util.Arrays.stream(ErrorCode.values()).filter(e -> e.code() == code).findFirst();
        if (enumCode.isPresent()) {
            String fromBundle = messageSource.getMessage(enumCode.get().messageKey(), null, null, locale);
            if (fromBundle != null) {
                return fromBundle;
            }
            return enumCode.get().message();
        }
        return "错误码 " + code;
    }
}
