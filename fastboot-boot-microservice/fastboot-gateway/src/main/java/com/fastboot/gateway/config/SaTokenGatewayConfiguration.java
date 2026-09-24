package com.fastboot.gateway.config;

import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.fastboot.common.error.ErrorCode;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 网关 Sa-Token 登录校验配置。
 *
 * @author zhaodt
 * @since 2026-09-24
 */
@Configuration
@EnableConfigurationProperties(SaTokenGatewayConfiguration.GatewaySecurityProperties.class)
public class SaTokenGatewayConfiguration {

    @Bean
    public SaReactorFilter saReactorFilter(GatewaySecurityProperties properties) {
        SaReactorFilter filter =
                new SaReactorFilter()
                        .addInclude("/**")
                        .setAuth(obj -> SaRouter.match("/**").check(r -> StpUtil.checkLogin()))
                        .setError(
                                e ->
                                        SaResult.error(ErrorCode.UNAUTHORIZED.message())
                                                .setCode(ErrorCode.UNAUTHORIZED.code()));
        for (String path : properties.getPermitPaths()) {
            filter.addExclude(path);
        }
        // 禁止经网关转发内部 API（仍排除登录校验，下游亦不应暴露）
        filter.addExclude("/api/v1/internal/**");
        return filter;
    }

    @ConfigurationProperties(prefix = "fastboot.security")
    public static class GatewaySecurityProperties {
        private List<String> permitPaths =
                new ArrayList<>(
                        List.of(
                                "/api/v1/auth/login",
                                "/actuator",
                                "/actuator/**",
                                "/v3/api-docs",
                                "/v3/api-docs/**",
                                "/swagger-ui",
                                "/swagger-ui/**",
                                "/swagger-ui.html"));

        public List<String> getPermitPaths() {
            return permitPaths;
        }

        public void setPermitPaths(List<String> permitPaths) {
            this.permitPaths = permitPaths;
        }
    }
}
