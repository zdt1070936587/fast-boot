package com.fastboot.modules.user.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fastboot.modules.user.infrastructure.persistence.entity.SysUserEntity;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统用户表 MyBatis 映射接口。
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUserEntity> {

    default Optional<SysUserEntity> findByUsername(String username) {
        return Optional.ofNullable(
                selectOne(Wrappers.<SysUserEntity>lambdaQuery().eq(SysUserEntity::getUsername, username)));
    }
}
