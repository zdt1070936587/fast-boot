package com.fastboot.modules.authz.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import java.time.OffsetDateTime;

/**
 * 系统角色持久化实体
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@TableName("sys_role")
public class SysRoleEntity {
  /** 主键 ID */
  @TableId(type = IdType.AUTO)
  private Long id;

  /** 角色编码 */
  private String code;
  /** 角色名称 */
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

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }
}
