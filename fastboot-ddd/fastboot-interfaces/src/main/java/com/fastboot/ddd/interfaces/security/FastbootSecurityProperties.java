package com.fastboot.ddd.interfaces.security;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * FastBoot 安全相关配置。
 *
 * @author zhaodt
 * @since 2026-09-24
 */
@ConfigurationProperties(prefix = "fastboot.security")
public class FastbootSecurityProperties {

    /** 是否放行 OpenAPI/Swagger 文档路径 */
    private boolean permitDocs;
    /** 是否放行内部 API 路径（仍需 X-Internal-Token） */
    private boolean permitInternal;
    /** 内部服务调用共享令牌 */
    private String internalToken = "";
    /** Sa-Token 登录校验白名单 */
    private List<String> permitPaths = defaultPermitPaths();

    private static List<String> defaultPermitPaths() {
        List<String> paths = new ArrayList<>();
        paths.add("/api/v1/auth/login");
        paths.add("/error");
        paths.add("/actuator");
        paths.add("/actuator/**");
        return paths;
    }

    public boolean isPermitDocs() {
        return permitDocs;
    }

    public void setPermitDocs(boolean permitDocs) {
        this.permitDocs = permitDocs;
    }

    public boolean isPermitInternal() {
        return permitInternal;
    }

    public void setPermitInternal(boolean permitInternal) {
        this.permitInternal = permitInternal;
    }

    public String getInternalToken() {
        return internalToken;
    }

    public void setInternalToken(String internalToken) {
        this.internalToken = internalToken;
    }

    public List<String> getPermitPaths() {
        return permitPaths;
    }

    public void setPermitPaths(List<String> permitPaths) {
        this.permitPaths = permitPaths;
    }
}
