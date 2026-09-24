package com.fastboot.ddd.interfaces.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fastboot.common.api.R;
import com.fastboot.common.error.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 内部 API 鉴权过滤器：校验 {@code X-Internal-Token}。
 *
 * @author zhaodt
 * @since 2026-09-24
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class InternalApiAuthFilter extends OncePerRequestFilter {

    public static final String HEADER = "X-Internal-Token";

    private final FastbootSecurityProperties securityProperties;
    private final ObjectMapper objectMapper;

    public InternalApiAuthFilter(FastbootSecurityProperties securityProperties, ObjectMapper objectMapper) {
        this.securityProperties = securityProperties;
        this.objectMapper = objectMapper;
        if (securityProperties.isPermitInternal()
                && (securityProperties.getInternalToken() == null
                        || securityProperties.getInternalToken().isBlank())) {
            throw new IllegalStateException(
                    "fastboot.security.permit-internal=true 时必须配置 fastboot.security.internal-token");
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path == null || !path.startsWith("/api/v1/internal/");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (!securityProperties.isPermitInternal()) {
            writeJson(response, HttpServletResponse.SC_NOT_FOUND, ErrorCode.NOT_FOUND);
            return;
        }
        String provided = request.getHeader(HEADER);
        String expected = securityProperties.getInternalToken();
        if (!constantTimeEquals(provided, expected)) {
            writeJson(response, HttpServletResponse.SC_UNAUTHORIZED, ErrorCode.UNAUTHORIZED);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private void writeJson(HttpServletResponse response, int status, ErrorCode error) throws IOException {
        response.setStatus(status);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getOutputStream(), R.fail(error.code(), error.message()));
    }

    private static boolean constantTimeEquals(String a, String b) {
        if (a == null || b == null) {
            return false;
        }
        byte[] left = a.getBytes(StandardCharsets.UTF_8);
        byte[] right = b.getBytes(StandardCharsets.UTF_8);
        return MessageDigest.isEqual(left, right);
    }
}
