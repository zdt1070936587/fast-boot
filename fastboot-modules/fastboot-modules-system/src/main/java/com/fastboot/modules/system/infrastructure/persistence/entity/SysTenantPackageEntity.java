package com.fastboot.modules.system.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import java.time.OffsetDateTime;

@TableName("sys_tenant_package")
public class SysTenantPackageEntity {
    @TableId(type = IdType.AUTO)
    private Long id;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    private String name;
    private String menuIds;
    @TableField(fill = FieldFill.INSERT)
    private OffsetDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private OffsetDateTime updatedAt;
    @Version
    private Long version;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getMenuIds() { return menuIds; }
    public void setMenuIds(String menuIds) { this.menuIds = menuIds; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }

}