package com.fastboot.modules.user.application;

import cn.dev33.satoken.stp.StpUtil;
import com.fastboot.common.audit.OperationLog;
import com.fastboot.common.error.BizException;
import com.fastboot.common.error.ErrorCode;
import com.fastboot.ddd.domain.event.DomainEventPublisher;
import com.fastboot.modules.authz.api.MenuQueryPort;
import com.fastboot.modules.authz.api.MenuSnapshot;
import com.fastboot.modules.authz.api.PermissionQueryPort;
import com.fastboot.modules.system.api.TenantQueryPort;
import com.fastboot.modules.user.domain.PasswordHasher;
import com.fastboot.modules.user.domain.User;
import com.fastboot.modules.user.domain.UserRepository;
import com.fastboot.modules.user.domain.event.UserLoggedIn;
import com.fastboot.modules.user.domain.event.UserLoggedOut;
import com.fastboot.starter.tenant.TenantContext;
import com.fastboot.starter.tenant.TenantResolver;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 认证应用服务：登录、登出、当前用户与组织归属。
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@Service
public class AuthAppService {

    /** 用户仓储 */
    private final UserRepository userRepository;
    /** 密码哈希器 */
    private final PasswordHasher passwordHasher;
    /** 权限查询端口 */
    private final PermissionQueryPort permissionQueryPort;
    /** 菜单查询端口 */
    private final MenuQueryPort menuQueryPort;
    /** 登录防护（限流与失败锁定） */
    private final LoginGuard loginGuard;
    /** 领域事件发布器 */
    private final DomainEventPublisher events;
    /** 可选租户查询 */
    private final ObjectProvider<TenantQueryPort> tenantQueryPort;
    /** 可选租户解析 */
    private final ObjectProvider<TenantResolver> tenantResolver;

    public AuthAppService(
            UserRepository userRepository,
            PasswordHasher passwordHasher,
            PermissionQueryPort permissionQueryPort,
            MenuQueryPort menuQueryPort,
            LoginGuard loginGuard,
            DomainEventPublisher events,
            ObjectProvider<TenantQueryPort> tenantQueryPort,
            ObjectProvider<TenantResolver> tenantResolver) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.permissionQueryPort = permissionQueryPort;
        this.menuQueryPort = menuQueryPort;
        this.loginGuard = loginGuard;
        this.events = events;
        this.tenantQueryPort = tenantQueryPort;
        this.tenantResolver = tenantResolver;
    }

    @OperationLog(value = "login", args = {"#username", "#clientIp"})
    public String login(String username, String password, String clientIp, HttpServletRequest request) {
        Long tenantId = resolveTenantId(request);
        TenantContext.set(tenantId);
        try {
            validateTenant(tenantId);
            loginGuard.assertAllowed(username, clientIp);
            try {
                User user =
                        userRepository
                                .findByUsername(username)
                                .orElseThrow(() -> new BizException(ErrorCode.USER_OR_PASSWORD));
                user.assertCanLogin(password, passwordHasher);
                StpUtil.login(user.getId());
                // SaSession 底层 ConcurrentHashMap 不允许 null value
                if (user.getOrgUnitId() != null) {
                    StpUtil.getSession().set("orgUnitId", user.getOrgUnitId());
                } else {
                    StpUtil.getSession().delete("orgUnitId");
                }
                StpUtil.getSession().set("tenantId", tenantId);
                permissionQueryPort.evict(user.getId());
                events.publish(new UserLoggedIn(user.getId(), user.getUsername()));
                loginGuard.onSuccess(username);
                return StpUtil.getTokenValue();
            } catch (BizException ex) {
                if (ex.getCode() == ErrorCode.USER_OR_PASSWORD.code()
                        || ex.getCode() == ErrorCode.ACCOUNT_DISABLED.code()) {
                    loginGuard.onFailure(username);
                }
                throw ex;
            }
        } finally {
            TenantContext.clear();
        }
    }

    @OperationLog("logout")
    public void logout() {
        if (StpUtil.isLogin()) {
            Long id = StpUtil.getLoginIdAsLong();
            StpUtil.logout();
            permissionQueryPort.evict(id);
            events.publish(new UserLoggedOut(id));
        }
    }

    @Transactional(readOnly = true)
    public java.util.List<MenuSnapshot> currentMenus() {
        Long id = StpUtil.getLoginIdAsLong();
        return menuQueryPort.listByUserId(id);
    }

    @Transactional(readOnly = true)
    public User currentUser() {
        Long id = StpUtil.getLoginIdAsLong();
        return userRepository
                .findById(id)
                .orElseThrow(() -> new BizException(ErrorCode.UNAUTHORIZED));
    }

    @Transactional
    public void assignOrg(Long userId, Long orgUnitId) {
        User user =
                userRepository
                        .findById(userId)
                        .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND));
        user.assignOrg(orgUnitId);
        userRepository.save(user);
    }

    private Long resolveTenantId(HttpServletRequest request) {
        TenantResolver resolver = tenantResolver.getIfAvailable();
        if (resolver != null && request != null) {
            Long resolved = resolver.resolve(request);
            if (resolved != null) {
                return resolved;
            }
        }
        return 1L;
    }

    private void validateTenant(Long tenantId) {
        TenantQueryPort port = tenantQueryPort.getIfAvailable();
        if (port == null) {
            return;
        }
        port.find(tenantId)
                .filter(TenantQueryPort.TenantSnapshot::active)
                .orElseThrow(() -> new BizException(ErrorCode.TENANT_INVALID));
    }
}
