package com.fastboot.service.user.authz;

import com.fastboot.ddd.interfaces.security.InternalApiAuthFilter;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 为 Feign 调用注入内部服务令牌。
 *
 * @author zhaodt
 * @since 2026-09-24
 */
@Component
public class InternalTokenFeignInterceptor implements RequestInterceptor {

    private final String internalToken;

    public InternalTokenFeignInterceptor(
            @Value("${fastboot.security.internal-token:}") String internalToken) {
        this.internalToken = internalToken;
    }

    @Override
    public void apply(RequestTemplate template) {
        if (internalToken != null && !internalToken.isBlank()) {
            template.header(InternalApiAuthFilter.HEADER, internalToken);
        }
    }
}
