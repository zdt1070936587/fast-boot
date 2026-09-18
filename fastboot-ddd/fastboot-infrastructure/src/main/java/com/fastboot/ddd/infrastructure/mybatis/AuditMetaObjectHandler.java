package com.fastboot.ddd.infrastructure.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import java.time.OffsetDateTime;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

@Component
public class AuditMetaObjectHandler implements MetaObjectHandler {

  @Override
  public void insertFill(MetaObject metaObject) {
    OffsetDateTime now = OffsetDateTime.now();
    strictInsertFill(metaObject, "createdAt", OffsetDateTime.class, now);
    strictInsertFill(metaObject, "updatedAt", OffsetDateTime.class, now);
  }

  @Override
  public void updateFill(MetaObject metaObject) {
    strictUpdateFill(metaObject, "updatedAt", OffsetDateTime.class, OffsetDateTime.now());
  }
}
