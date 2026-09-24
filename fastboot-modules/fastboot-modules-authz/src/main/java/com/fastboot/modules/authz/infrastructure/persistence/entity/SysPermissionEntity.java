package com.fastboot.modules.authz.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import java.time.OffsetDateTime;

/**
 * 系统权限点持久化实体
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@TableName("sys_permission")
public class SysPermissionEntity {
    /** 主键 ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 权限类型（菜单/按钮/API/数据） */
    private String permType;
    /** 权限编码，用于鉴权匹配 */
    private String permCode;
    /** 权限显示名称 */
    private String name;
    /** 乐观锁版本号 */
    @Version private Long version;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private OffsetDateTime createdAt;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private OffsetDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPermType() {
        return permType;
    }

    public void setPermType(String permType) {
        this.permType = permType;
    }

    public String getPermCode() {
        return permCode;
    }

    public void setPermCode(String permCode) {
        this.permCode = permCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
