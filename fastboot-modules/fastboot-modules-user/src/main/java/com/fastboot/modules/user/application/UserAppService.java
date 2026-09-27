package com.fastboot.modules.user.application;

import com.fastboot.common.error.BizException;
import com.fastboot.common.error.ErrorCode;
import com.fastboot.modules.user.application.dto.UserView;
import com.fastboot.modules.user.domain.PasswordHasher;
import com.fastboot.modules.user.domain.User;
import com.fastboot.modules.user.domain.UserRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户管理应用服务。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@Service
public class UserAppService {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public UserAppService(UserRepository userRepository, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    @Transactional(readOnly = true)
    public List<UserView> list() {
        return userRepository.findAll().stream().map(this::toView).toList();
    }

    @Transactional(readOnly = true)
    public UserView get(Long id) {
        User user =
                userRepository.findById(id).orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND));
        return toView(user);
    }

    @Transactional
    public Long create(String username, String displayName, String rawPassword, Long orgUnitId) {
        userRepository
                .findByUsername(username)
                .ifPresent(u -> {
                    throw new BizException(ErrorCode.CONFLICT, "用户名已存在");
                });
        String hash = passwordHasher.hash(rawPassword);
        User user = User.create(username, displayName, hash, orgUnitId);
        userRepository.save(user);
        return user.getId();
    }

    @Transactional
    public void update(Long id, String displayName, Boolean enabled, Long orgUnitId) {
        User user =
                userRepository.findById(id).orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND));
        user.updateProfile(displayName, enabled, orgUnitId);
        userRepository.save(user);
    }

    @Transactional
    public void resetPassword(Long id, String rawPassword) {
        User user =
                userRepository.findById(id).orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND));
        user.resetPassword(passwordHasher.hash(rawPassword));
        userRepository.save(user);
    }

    @Transactional
    public void delete(Long id) {
        if (userRepository.findById(id).isEmpty()) {
            throw new BizException(ErrorCode.NOT_FOUND);
        }
        userRepository.deleteById(id);
    }

    private UserView toView(User user) {
        return new UserView(
                user.getId(),
                user.getUsername(),
                user.getDisplayName(),
                user.isEnabled(),
                user.getOrgUnitId());
    }
}
