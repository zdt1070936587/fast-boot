package com.fastboot.ddd.domain.model;

import java.io.Serializable;
import java.time.OffsetDateTime;

public abstract class AggregateRoot<ID extends Serializable> implements Serializable {
  private ID id;
  private Long version;
  private OffsetDateTime createdAt;
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
