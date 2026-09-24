package com.fastboot.modules.org.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fastboot.modules.org.infrastructure.persistence.entity.SysOrgUnitEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 组织单元 MyBatis 映射器
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@Mapper
public interface SysOrgUnitMapper extends BaseMapper<SysOrgUnitEntity> {

    @Update(
            """
            update sys_org_unit
               set path = #{newPrefix} || substring(path from length(#{oldPrefix}) + 1),
                   version = version + 1,
                   updated_at = now()
             where path like #{oldPrefix} || '%'
               and id <> #{selfId}
            """)
    int rebaseSubtreePath(
            @Param("oldPrefix") String oldPrefix,
            @Param("newPrefix") String newPrefix,
            @Param("selfId") Long selfId);
}
