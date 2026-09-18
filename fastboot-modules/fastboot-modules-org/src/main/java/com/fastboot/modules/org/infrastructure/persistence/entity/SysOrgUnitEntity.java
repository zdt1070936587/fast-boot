package com.fastboot.modules.org.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import java.time.OffsetDateTime;

/**
 * 组织单元持久化实体
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@TableName("sys_org_unit")
public class SysOrgUnitEntity {
  /** 主键 ID */
  @TableId(type = IdType.AUTO)
  private Long id;

  /** 上级组织 ID */
  private Long parentId;
  /** 组织名称 */
  private String name;
  /** 物化路径 */
  private String path;
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

  public Long getParentId() {
    return parentId;
  }

  public void setParentId(Long parentId) {
    this.parentId = parentId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }
}
