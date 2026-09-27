package com.fastboot.ddd.infrastructure.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import java.util.ArrayList;
import java.util.List;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

/**
 * MyBatis-Plus 全局配置：按 {@link org.springframework.core.annotation.Order} 收集外部 InnerInterceptor，再挂分页/乐观锁/防全表更新。
 *
 * <p>约定顺序：TenantLine(1) → DataPermission(2) → Pagination(3) → OptimisticLocker(4) → BlockAttack(5)。
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@Configuration
@MapperScan(basePackages = "com.fastboot.**.infrastructure.mapper")
public class MybatisPlusConfig {

    /**
     * 注册插件链：先外部（租户/数据权限），再内置。
     *
     * <p>使用 {@code getIfAvailable} 逐个取 Bean，避免在 sqlSessionFactory 创建阶段
     * 因 {@code orderedStream} 过早实例化依赖业务 Mapper 的拦截器。
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(ObjectProvider<InnerInterceptor> extras) {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        List<InnerInterceptor> list = new ArrayList<>();
        extras.forEach(list::add);
        list.sort(AnnotationAwareOrderComparator.INSTANCE);
        for (InnerInterceptor extra : list) {
            if (isBuiltin(extra)) {
                continue;
            }
            interceptor.addInnerInterceptor(extra);
        }
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.POSTGRE_SQL));
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        return interceptor;
    }

    private boolean isBuiltin(InnerInterceptor interceptor) {
        return interceptor instanceof PaginationInnerInterceptor
                || interceptor instanceof OptimisticLockerInnerInterceptor
                || interceptor instanceof BlockAttackInnerInterceptor;
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
