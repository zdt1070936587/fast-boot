package com.fastboot.modules.authz.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import java.time.OffsetDateTime;

/**
 * 系统菜单持久化实体
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@TableName("sys_menu")
public class SysMenuEntity {
  /** 主键 ID */
  @TableId(type = IdType.AUTO)
  private Long id;

  /** 父菜单 ID */
  private Long parentId;
  /** 菜单名称 */
  private String name;
  /** 前端路由路径 */
  private String path;
  /** 前端组件路径 */
  private String component;
  /** 访问所需权限编码 */
  private String permCode;
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

  public String getComponent() {
    return component;
  }

  public void setComponent(String component) {
    this.component = component;
  }

  public String getPermCode() {
    return permCode;
  }

  public void setPermCode(String permCode) {
    this.permCode = permCode;
  }
}
