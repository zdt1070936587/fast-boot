import os

BASE = r"E:\code\fast-boot\fastboot-modules\fastboot-modules-system\src\main\java\com\fastboot\modules\system\infrastructure\persistence\entity"
os.makedirs(BASE, exist_ok=True)


def write(name, table, fields):
    lines = [
        "package com.fastboot.modules.system.infrastructure.persistence.entity;",
        "",
        "import com.baomidou.mybatisplus.annotation.FieldFill;",
        "import com.baomidou.mybatisplus.annotation.IdType;",
        "import com.baomidou.mybatisplus.annotation.TableField;",
        "import com.baomidou.mybatisplus.annotation.TableId;",
        "import com.baomidou.mybatisplus.annotation.TableName;",
        "import com.baomidou.mybatisplus.annotation.Version;",
        "import java.time.OffsetDateTime;",
        "",
        f'@TableName("{table}")',
        f"public class {name} {{",
        "    @TableId(type = IdType.AUTO)",
        "    private Long id;",
    ]
    for ftype, fname in fields:
        if fname == "version":
            lines.append("    @Version")
        elif fname == "createdAt":
            lines.append("    @TableField(fill = FieldFill.INSERT)")
        elif fname == "updatedAt":
            lines.append("    @TableField(fill = FieldFill.INSERT_UPDATE)")
        lines.append(f"    private {ftype} {fname};")
    lines.append("")
    for ftype, fname in fields:
        cap = fname[0].upper() + fname[1:]
        lines += [
            f"    public {ftype} get{cap}() {{ return {fname}; }}",
            f"    public void set{cap}({ftype} {fname}) {{ this.{fname} = {fname}; }}",
            "",
        ]
    lines.append("}")
    with open(os.path.join(BASE, name + ".java"), "w", encoding="utf-8") as f:
        f.write("\n".join(lines))


std = [
    ("Long", "tenantId"),
    ("OffsetDateTime", "createdAt"),
    ("OffsetDateTime", "updatedAt"),
    ("Long", "createdBy"),
    ("Long", "updatedBy"),
    ("Long", "version"),
]
write("SysDictTypeEntity", "sys_dict_type", std + [("String", "code"), ("String", "name"), ("String", "remark")])
write(
    "SysDictDataEntity",
    "sys_dict_data",
    std
    + [
        ("String", "typeCode"),
        ("String", "label"),
        ("String", "value"),
        ("Integer", "sortNo"),
        ("Boolean", "enabled"),
    ],
)
write(
    "SysConfigEntity",
    "sys_config",
    std + [("String", "configKey"), ("String", "configValue"), ("String", "remark"), ("Boolean", "refreshable")],
)
write("SysNoticeEntity", "sys_notice", std + [("String", "title"), ("String", "content"), ("String", "status")])
write(
    "SysErrorCodeEntity",
    "sys_error_code",
    std + [("Integer", "code"), ("String", "locale"), ("String", "message")],
)
write("SysPostEntity", "sys_post", std + [("String", "code"), ("String", "name"), ("Integer", "sortNo")])
write(
    "SysOperationLogEntity",
    "sys_operation_log",
    [
        ("Long", "tenantId"),
        ("Long", "userId"),
        ("String", "action"),
        ("String", "method"),
        ("String", "args"),
        ("String", "result"),
        ("Long", "durationMs"),
        ("String", "traceId"),
        ("String", "clientIp"),
        ("String", "userAgent"),
        ("Boolean", "success"),
        ("Integer", "errorCode"),
        ("OffsetDateTime", "createdAt"),
        ("Long", "createdBy"),
    ],
)
write(
    "SysLoginLogEntity",
    "sys_login_log",
    [
        ("Long", "tenantId"),
        ("Long", "userId"),
        ("String", "username"),
        ("String", "eventType"),
        ("String", "clientIp"),
        ("String", "userAgent"),
        ("Boolean", "success"),
        ("String", "message"),
        ("OffsetDateTime", "createdAt"),
    ],
)
write(
    "SysTenantEntity",
    "sys_tenant",
    [
        ("String", "code"),
        ("String", "name"),
        ("Long", "packageId"),
        ("String", "status"),
        ("OffsetDateTime", "expireAt"),
        ("String", "contact"),
        ("OffsetDateTime", "createdAt"),
        ("OffsetDateTime", "updatedAt"),
        ("Long", "version"),
    ],
)
write(
    "SysTenantPackageEntity",
    "sys_tenant_package",
    [
        ("String", "name"),
        ("String", "menuIds"),
        ("OffsetDateTime", "createdAt"),
        ("OffsetDateTime", "updatedAt"),
        ("Long", "version"),
    ],
)
print("entities ok")
