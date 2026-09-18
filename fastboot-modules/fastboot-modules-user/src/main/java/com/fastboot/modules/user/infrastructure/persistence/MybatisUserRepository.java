package com.fastboot.modules.user.infrastructure.persistence;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
  public void updatePasswordHash(Long id, String passwordHash) {
    sysUserMapper.update(
        null,
        Wrappers.<SysUserEntity>lambdaUpdate()
            .set(SysUserEntity::getPasswordHash, passwordHash)
            .eq(SysUserEntity::getId, id));
  }

  @Override
  public void updateOrgUnit(Long id, Long orgUnitId) {
    sysUserMapper.update(
        null,
        Wrappers.<SysUserEntity>lambdaUpdate()
            .set(SysUserEntity::getOrgUnitId, orgUnitId)
            .eq(SysUserEntity::getId, id));
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
}
