package com.fastboot.ddd.interfaces.security;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Sa-Token MVC 拦截配置，默认全路径登录校验并支持白名单。
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EnableConfigurationProperties(FastbootSecurityProperties.class)
public class SaTokenMvcConfiguration implements WebMvcConfigurer {

    private final FastbootSecurityProperties securityProperties;

    public SaTokenMvcConfiguration(FastbootSecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    /** 注册 Sa-Token 拦截器及排除路径 */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> excludes = new ArrayList<>(securityProperties.getPermitPaths());
        if (securityProperties.isPermitDocs()) {
            excludes.add("/v3/api-docs");
            excludes.add("/v3/api-docs/**");
            excludes.add("/swagger-ui.html");
            excludes.add("/swagger-ui/**");
        }
        if (securityProperties.isPermitInternal()) {
            excludes.add("/api/v1/internal/**");
        }
        registry
                .addInterceptor(
                        new SaInterceptor(
                                handle ->
                                        SaRouter.match("/**")
                                                .notMatch(excludes.toArray(String[]::new))
                                                .check(r -> StpUtil.checkLogin())))
                .addPathPatterns("/**");
    }
}
