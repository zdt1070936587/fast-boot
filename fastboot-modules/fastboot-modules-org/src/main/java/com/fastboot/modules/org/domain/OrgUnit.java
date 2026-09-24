package com.fastboot.modules.org.domain;

import com.fastboot.common.error.BizException;
import com.fastboot.common.error.ErrorCode;
import com.fastboot.ddd.domain.model.AggregateRoot;

/**
 * 组织树节点聚合根
 *
 * @author zhaodt
 * @since 2026-09-19
 */
public class OrgUnit extends AggregateRoot<Long> {
    /** 上级组织 ID */
    private Long parentId;
    /** 组织名称 */
    private String name;
    /** 物化路径（用于子树查询与移动） */
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
        if (newParent == null || newParent.getId() == null) {
            throw new BizException(ErrorCode.NOT_FOUND, "上级组织不存在");
        }
        if (newParent.getId().equals(getId())) {
            throw new BizException(ErrorCode.CONFLICT, "不能移动到自身或子节点下");
        }
        String selfPath = path == null ? "" : path;
        String parentPath = newParent.getPath() == null ? "" : newParent.getPath();
        if (!selfPath.isBlank() && parentPath.startsWith(selfPath)) {
            throw new BizException(ErrorCode.CONFLICT, "不能移动到自身或子节点下");
        }
        this.parentId = newParent.getId();
        this.path = parentPath + getId() + "/";
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
