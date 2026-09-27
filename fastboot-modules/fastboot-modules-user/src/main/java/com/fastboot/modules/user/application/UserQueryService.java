package com.fastboot.modules.user.application;

import com.fastboot.modules.user.api.UserQueryPort;
import com.fastboot.modules.user.api.UserSnapshot;
import com.fastboot.modules.user.infrastructure.mapper.SysUserMapper;
import com.fastboot.modules.user.infrastructure.persistence.entity.SysUserEntity;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户查询端口适配器。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@Service
public class UserQueryService implements UserQueryPort {

    private final SysUserMapper sysUserMapper;

    public UserQueryService(SysUserMapper sysUserMapper) {
        this.sysUserMapper = sysUserMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserSnapshot> find(Long id) {
        return Optional.ofNullable(sysUserMapper.selectById(id)).map(this::toSnapshot);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserSnapshot> findByIds(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return sysUserMapper.findByIds(ids).stream().map(this::toSnapshot).toList();
    }

    private UserSnapshot toSnapshot(SysUserEntity entity) {
        return new UserSnapshot(
                entity.getId(),
                entity.getUsername(),
                entity.getDisplayName(),
                Boolean.TRUE.equals(entity.getEnabled()),
                entity.getOrgUnitId(),
                entity.getTenantId() != null ? entity.getTenantId() : 1L);
    }
}
