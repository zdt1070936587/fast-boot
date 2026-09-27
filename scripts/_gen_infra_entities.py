import os

BASE = r"E:\code\fast-boot\fastboot-modules\fastboot-modules-infra\src\main\java\com\fastboot\modules\infra\infrastructure\persistence\entity"
os.makedirs(BASE, exist_ok=True)


def write(name, table, fields):
    lines = [
        "package com.fastboot.modules.infra.infrastructure.persistence.entity;",
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
    lines += [
        "",
        "    public Long getId() { return id; }",
        "    public void setId(Long id) { this.id = id; }",
    ]
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
write(
    "SysFileConfigEntity",
    "sys_file_config",
    std + [("String", "name"), ("String", "storage"), ("Boolean", "master"), ("String", "config")],
)
write(
    "SysFileEntity",
    "sys_file",
    [
        ("Long", "tenantId"),
        ("Long", "configId"),
        ("String", "name"),
        ("String", "path"),
        ("String", "url"),
        ("Long", "sizeBytes"),
        ("String", "contentType"),
        ("OffsetDateTime", "createdAt"),
        ("Long", "createdBy"),
    ],
)
write(
    "SysNotifyTemplateEntity",
    "sys_notify_template",
    std + [("String", "code"), ("String", "channel"), ("String", "title"), ("String", "content")],
)
write(
    "SysNotifyMessageEntity",
    "sys_notify_message",
    [
        ("Long", "tenantId"),
        ("String", "templateCode"),
        ("String", "channel"),
        ("String", "receiver"),
        ("String", "title"),
        ("String", "content"),
        ("String", "status"),
        ("OffsetDateTime", "createdAt"),
    ],
)
write(
    "SysInboxEntity",
    "sys_inbox",
    [
        ("Long", "tenantId"),
        ("Long", "userId"),
        ("String", "title"),
        ("String", "content"),
        ("Boolean", "readFlag"),
        ("OffsetDateTime", "createdAt"),
    ],
)
write(
    "SysJobEntity",
    "sys_job",
    std + [("String", "name"), ("String", "handler"), ("String", "cronExpression"), ("String", "params"), ("Boolean", "enabled")],
)
write(
    "SysJobLogEntity",
    "sys_job_log",
    [
        ("Long", "tenantId"),
        ("Long", "jobId"),
        ("Boolean", "success"),
        ("String", "message"),
        ("Long", "durationMs"),
        ("OffsetDateTime", "createdAt"),
    ],
)
write(
    "SysApiAccessLogEntity",
    "sys_api_access_log",
    [
        ("Long", "tenantId"),
        ("Long", "userId"),
        ("String", "method"),
        ("String", "path"),
        ("String", "queryString"),
        ("Integer", "status"),
        ("Long", "durationMs"),
        ("String", "traceId"),
        ("String", "clientIp"),
        ("OffsetDateTime", "createdAt"),
    ],
)
write(
    "SysDataChangeLogEntity",
    "sys_data_change_log",
    [
        ("Long", "tenantId"),
        ("String", "aggregateType"),
        ("String", "aggregateId"),
        ("String", "beforeJson"),
        ("String", "afterJson"),
        ("Long", "createdBy"),
        ("OffsetDateTime", "createdAt"),
    ],
)
print("infra entities ok")
