package com.fastboot.ddd.domain.model;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * DDD 聚合根基类，封装标识、乐观锁版本与审计时间字段。
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
}
