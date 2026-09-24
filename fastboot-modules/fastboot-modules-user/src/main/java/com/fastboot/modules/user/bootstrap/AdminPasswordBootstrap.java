package com.fastboot.modules.user.bootstrap;

import com.fastboot.modules.user.domain.PasswordHasher;
import com.fastboot.modules.user.domain.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 应用启动时为未设置密码的 admin 用户初始化密码（需显式开启配置）。
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@Component
@Order(20)
@ConditionalOnProperty(name = "fastboot.admin.bootstrap", havingValue = "true")
public class AdminPasswordBootstrap implements ApplicationRunner {

    /** 日志记录器 */
    private static final Logger log = LoggerFactory.getLogger(AdminPasswordBootstrap.class);

    /** 用户仓储 */
    private final UserRepository userRepository;
    /** 密码哈希器 */
    private final PasswordHasher passwordHasher;
    /** 配置中的 admin 初始密码 */
    private final String adminPassword;

    public AdminPasswordBootstrap(
            UserRepository userRepository,
            PasswordHasher passwordHasher,
            @Value("${fastboot.admin.password:}") String adminPassword) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.adminPassword = adminPassword;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (adminPassword == null || adminPassword.isBlank()) {
            throw new IllegalStateException("fastboot.admin.bootstrap=true 时必须设置 fastboot.admin.password");
        }
        userRepository
                .findByUsername("admin")
                .ifPresent(
                        user -> {
                            if (!user.hasPassword()) {
                                user.initializePassword(passwordHasher.hash(adminPassword));
                                userRepository.save(user);
                                log.warn("local profile: initialized admin password from configuration");
                            }
                        });
    }
}
