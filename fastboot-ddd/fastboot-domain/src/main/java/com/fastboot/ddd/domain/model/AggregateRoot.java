package com.fastboot.ddd.domain.model;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * DDD 聚合根基类，封装标识、乐观锁版本、审计时间字段与领域事件收集。
 *
 * @author zhaodt
 * @since 2026-09-19
 */
public abstract class AggregateRoot<ID extends Serializable> implements Serializable {
    /** 聚合根主键 */
    private ID id;
    /** 乐观锁版本号 */
    private Long version;
    /** 创建时间 */
    private OffsetDateTime createdAt;
    /** 最后更新时间 */
    private OffsetDateTime updatedAt;
    /** 创建人 */
    private Long createdBy;
    /** 更新人 */
    private Long updatedBy;
    /** 待发布的领域事件 */
    private transient List<Object> domainEvents = new ArrayList<>();

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    protected void registerEvent(Object event) {
        if (domainEvents == null) {
            domainEvents = new ArrayList<>();
        }
        domainEvents.add(event);
    }

    /** 拉取并清空已注册的领域事件 */
    public List<Object> pullEvents() {
        if (domainEvents == null || domainEvents.isEmpty()) {
            return List.of();
        }
        List<Object> copy = List.copyOf(domainEvents);
        domainEvents.clear();
        return copy;
    }

    public List<Object> peekEvents() {
        if (domainEvents == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(domainEvents);
    }
}
