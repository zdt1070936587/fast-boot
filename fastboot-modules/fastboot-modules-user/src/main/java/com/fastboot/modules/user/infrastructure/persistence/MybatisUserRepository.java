package com.fastboot.modules.user.infrastructure.persistence;

import com.fastboot.common.error.BizException;
import com.fastboot.common.error.ErrorCode;
import com.fastboot.modules.user.domain.User;
import com.fastboot.modules.user.domain.UserRepository;
import com.fastboot.modules.user.infrastructure.mapper.SysUserMapper;
import com.fastboot.modules.user.infrastructure.persistence.entity.SysUserEntity;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * 基于 MyBatis-Plus 的 {@link UserRepository} 实现。
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@Repository
public class MybatisUserRepository implements UserRepository {

    /** 系统用户 Mapper */
    private final SysUserMapper sysUserMapper;

    public MybatisUserRepository(SysUserMapper sysUserMapper) {
        this.sysUserMapper = sysUserMapper;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return sysUserMapper.findByUsername(username).map(this::toDomain);
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(sysUserMapper.selectById(id)).map(this::toDomain);
    }

    @Override
    public User save(User user) {
        SysUserEntity entity = toEntity(user);
        if (entity.getId() == null) {
            sysUserMapper.insert(entity);
            user.setId(entity.getId());
            user.setVersion(entity.getVersion());
            return user;
        }
        int updated = sysUserMapper.updateById(entity);
        if (updated == 0) {
            throw new BizException(ErrorCode.CONFLICT, "数据已被修改");
        }
        SysUserEntity fresh = sysUserMapper.selectById(entity.getId());
        if (fresh != null) {
            user.setVersion(fresh.getVersion());
        }
        return user;
    }

    private User toDomain(SysUserEntity entity) {
        return User.restore(
                entity.getId(),
                entity.getUsername(),
                entity.getDisplayName(),
                entity.getPasswordHash(),
                Boolean.TRUE.equals(entity.getEnabled()),
                entity.getOrgUnitId(),
                entity.getVersion());
    }

    private SysUserEntity toEntity(User user) {
        SysUserEntity entity = new SysUserEntity();
        entity.setId(user.getId());
        entity.setUsername(user.getUsername());
        entity.setDisplayName(user.getDisplayName());
        entity.setPasswordHash(user.getPasswordHash());
        entity.setEnabled(user.isEnabled());
        entity.setOrgUnitId(user.getOrgUnitId());
        entity.setVersion(user.getVersion());
        return entity;
    }
}
