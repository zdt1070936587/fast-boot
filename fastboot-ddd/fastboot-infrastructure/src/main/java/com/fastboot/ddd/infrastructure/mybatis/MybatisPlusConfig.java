package com.fastboot.ddd.infrastructure.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis-Plus 全局配置：分页、乐观锁、防全表更新及日志实现。
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@Configuration
@MapperScan(basePackages = "com.fastboot.**.infrastructure.mapper")
public class MybatisPlusConfig {

  /** 注册分页、乐观锁与 block-attack 插件 */
  @Bean
  public MybatisPlusInterceptor mybatisPlusInterceptor() {
    MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
    interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.POSTGRE_SQL));
    interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
    interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
    return interceptor;
  }

  /** 驼峰映射与 SLF4J 日志实现 */
  @Bean
  public ConfigurationCustomizer configurationCustomizer() {
    return configuration -> {
      configuration.setMapUnderscoreToCamelCase(true);
      configuration.setLogImpl(Slf4jImpl.class);
    };
  }
}
