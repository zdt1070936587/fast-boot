package com.fastboot.ddd.interfaces.security;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class SaTokenMvcConfiguration implements WebMvcConfigurer {

  @Value("${fastboot.security.permit-docs:false}")
  private boolean permitDocs;

  @Value("${fastboot.security.permit-internal:false}")
  private boolean permitInternal;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    List<String> excludes = new ArrayList<>();
    excludes.add("/api/v1/auth/login");
    excludes.add("/error");
    excludes.add("/actuator");
    excludes.add("/actuator/**");
    if (permitDocs) {
      excludes.add("/v3/api-docs");
      excludes.add("/v3/api-docs/**");
      excludes.add("/swagger-ui.html");
      excludes.add("/swagger-ui/**");
    }
    if (permitInternal) {
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
