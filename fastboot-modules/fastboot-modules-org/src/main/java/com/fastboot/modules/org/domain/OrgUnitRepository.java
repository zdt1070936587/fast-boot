package com.fastboot.modules.org.domain;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * 组织单元仓储接口
 *
 * @author zhaodt
 * @since 2026-09-19
 */
public interface OrgUnitRepository {
    OrgUnit save(OrgUnit unit);

    Optional<OrgUnit> findById(Long id);

    List<OrgUnit> findAll();

    List<OrgUnit> findByIds(Collection<Long> ids);

    int rebaseSubtreePath(String oldPrefix, String newPrefix, Long selfId);

    void deleteById(Long id);
}
