import os

MAPPERS = [
    ("system", "SysDictTypeEntity", "SysDictTypeMapper"),
    ("system", "SysDictDataEntity", "SysDictDataMapper"),
    ("system", "SysConfigEntity", "SysConfigMapper"),
    ("system", "SysNoticeEntity", "SysNoticeMapper"),
    ("system", "SysErrorCodeEntity", "SysErrorCodeMapper"),
    ("system", "SysOperationLogEntity", "SysOperationLogMapper"),
    ("system", "SysLoginLogEntity", "SysLoginLogMapper"),
    ("system", "SysPostEntity", "SysPostMapper"),
    ("system", "SysTenantEntity", "SysTenantMapper"),
    ("system", "SysTenantPackageEntity", "SysTenantPackageMapper"),
    ("infra", "SysFileConfigEntity", "SysFileConfigMapper"),
    ("infra", "SysFileEntity", "SysFileMapper"),
    ("infra", "SysNotifyTemplateEntity", "SysNotifyTemplateMapper"),
    ("infra", "SysNotifyMessageEntity", "SysNotifyMessageMapper"),
    ("infra", "SysInboxEntity", "SysInboxMapper"),
    ("infra", "SysJobEntity", "SysJobMapper"),
    ("infra", "SysJobLogEntity", "SysJobLogMapper"),
    ("infra", "SysApiAccessLogEntity", "SysApiAccessLogMapper"),
    ("infra", "SysDataChangeLogEntity", "SysDataChangeLogMapper"),
]

ROOT = r"E:\code\fast-boot\fastboot-modules"

for mod, entity, mapper in MAPPERS:
    pkg = f"com.fastboot.modules.{mod}.infrastructure.mapper"
    ent_pkg = f"com.fastboot.modules.{mod}.infrastructure.persistence.entity"
    base = os.path.join(ROOT, f"fastboot-modules-{mod}", "src", "main", "java", *pkg.split("."))
    os.makedirs(base, exist_ok=True)
    content = f"""package {pkg};

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.{ent_pkg.replace('.', '.') if False else ent_pkg};
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface {mapper} extends BaseMapper<{entity.split('Entity')[0]}Entity> {{
}}
"""
    # fix import
    content = f"""package {pkg};

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import {ent_pkg}.{entity};
import org.apache.ibatis.annotations.Mapper;

/**
 * {entity} Mapper。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@Mapper
public interface {mapper} extends BaseMapper<{entity}> {{
}}
"""
    with open(os.path.join(base, mapper + ".java"), "w", encoding="utf-8") as f:
        f.write(content)
print("mappers ok")
