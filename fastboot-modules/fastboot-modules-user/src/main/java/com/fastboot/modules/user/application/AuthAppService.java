package com.fastboot.modules.user.application;

import cn.dev33.satoken.stp.StpUtil;
import com.fastboot.common.audit.OperationLog;
import com.fastboot.common.error.BizException;
import com.fastboot.common.error.ErrorCode;
import com.fastboot.modules.authz.api.PermissionQueryPort;
import com.fastboot.modules.user.domain.PasswordHasher;
import com.fastboot.modules.user.domain.User;
import com.fastboot.modules.user.domain.UserRepository;
import com.fastboot.modules.user.domain.event.UserLoggedIn;
import com.fastboot.modules.user.domain.event.UserLoggedOut;
import org.springframework.context.ApplicationEventPublisher;
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
    /** 登录防护（限流与失败锁定） */
    private final LoginGuard loginGuard;
    /** 领域事件发布器 */
    private final ApplicationEventPublisher events;

    public AuthAppService(
            UserRepository userRepository,
            PasswordHasher passwordHasher,
            PermissionQueryPort permissionQueryPort,
            LoginGuard loginGuard,
            ApplicationEventPublisher events) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.permissionQueryPort = permissionQueryPort;
        this.loginGuard = loginGuard;
        this.events = events;
    }

    @Transactional(readOnly = true)
    @OperationLog("login")
    public String login(String username, String password, String clientIp) {
        loginGuard.assertAllowed(username, clientIp);
        try {
            User user =
                    userRepository
                            .findByUsername(username)
                            .orElseThrow(() -> new BizException(ErrorCode.USER_OR_PASSWORD));
            user.assertCanLogin(password, passwordHasher);
            StpUtil.login(user.getId());
            permissionQueryPort.evict(user.getId());
            events.publishEvent(new UserLoggedIn(user.getId(), user.getUsername()));
            loginGuard.onSuccess(username);
            return StpUtil.getTokenValue();
        } catch (BizException ex) {
            if (ex.getCode() == ErrorCode.USER_OR_PASSWORD.code()
                    || ex.getCode() == ErrorCode.ACCOUNT_DISABLED.code()) {
                loginGuard.onFailure(username);
            }
            throw ex;
        }
    }

    @OperationLog("logout")
    public void logout() {
        if (StpUtil.isLogin()) {
            Long id = StpUtil.getLoginIdAsLong();
            StpUtil.logout();
            permissionQueryPort.evict(id);
            events.publishEvent(new UserLoggedOut(id));
        }
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
        userRepository.updateOrgUnit(userId, orgUnitId);
    }
}
