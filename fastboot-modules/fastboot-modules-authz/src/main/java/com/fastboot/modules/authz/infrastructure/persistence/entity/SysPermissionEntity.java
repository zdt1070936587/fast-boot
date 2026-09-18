package com.fastboot.modules.authz.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import java.time.OffsetDateTime;

@TableName("sys_permission")
public class SysPermissionEntity {
  @TableId(type = IdType.AUTO)
  private Long id;

  private String permType;
  private String permCode;
  private String name;
  @Version private Long version;

  @TableField(fill = FieldFill.INSERT)
  private OffsetDateTime createdAt;

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
