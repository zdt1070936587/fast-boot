package com.fastboot.modules.org.domain;

import com.fastboot.common.error.BizException;
import com.fastboot.common.error.ErrorCode;
import com.fastboot.ddd.domain.model.AggregateRoot;

public class OrgUnit extends AggregateRoot<Long> {
  private Long parentId;
  private String name;
  private String path;

  public static OrgUnit create(Long parentId, String name, String parentPath) {
    OrgUnit unit = new OrgUnit();
    unit.parentId = parentId;
    unit.name = name;
    unit.path = parentPath == null || parentPath.isBlank() ? "" : parentPath;
    return unit;
  }

  public static OrgUnit restore(Long id, Long parentId, String name, String path, Long version) {
    OrgUnit unit = new OrgUnit();
    unit.setId(id);
    unit.parentId = parentId;
    unit.name = name;
    unit.path = path;
    unit.setVersion(version);
    return unit;
  }

  public void assignIdToPath() {
    if (getId() == null) {
      throw new BizException(ErrorCode.INTERNAL_ERROR, "组织尚未持久化");
    }
    String prefix = path == null ? "" : path;
    if (!prefix.endsWith("/") && !prefix.isBlank()) {
      prefix = prefix + "/";
    }
    this.path = prefix + getId() + "/";
  }

  public void moveTo(OrgUnit newParent) {
    this.parentId = newParent.getId();
    this.path = newParent.getPath() + getId() + "/";
  }

  public Long getParentId() {
    return parentId;
  }

  public String getName() {
    return name;
  }

  public void rename(String name) {
    this.name = name;
  }

  public String getPath() {
    return path;
  }
}
