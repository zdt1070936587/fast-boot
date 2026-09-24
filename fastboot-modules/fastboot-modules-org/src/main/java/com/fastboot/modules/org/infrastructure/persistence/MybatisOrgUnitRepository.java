package com.fastboot.modules.org.infrastructure.persistence;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fastboot.common.error.BizException;
import com.fastboot.common.error.ErrorCode;
import com.fastboot.modules.org.domain.OrgUnit;
import com.fastboot.modules.org.domain.OrgUnitRepository;
import com.fastboot.modules.org.infrastructure.mapper.SysOrgUnitMapper;
import com.fastboot.modules.org.infrastructure.persistence.entity.SysOrgUnitEntity;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * 组织单元 MyBatis 仓储实现
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@Repository
public class MybatisOrgUnitRepository implements OrgUnitRepository {

    /** 组织单元表映射器 */
    private final SysOrgUnitMapper mapper;

    public MybatisOrgUnitRepository(SysOrgUnitMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public OrgUnit save(OrgUnit unit) {
        SysOrgUnitEntity entity = toEntity(unit);
        if (entity.getId() == null) {
            mapper.insert(entity);
            unit.setId(entity.getId());
            unit.setVersion(entity.getVersion());
            return unit;
        }
        int updated = mapper.updateById(entity);
        if (updated == 0) {
            throw new BizException(ErrorCode.CONFLICT, "数据已被修改");
        }
        SysOrgUnitEntity fresh = mapper.selectById(entity.getId());
        if (fresh != null) {
            unit.setVersion(fresh.getVersion());
        }
        return unit;
    }

    @Override
    public Optional<OrgUnit> findById(Long id) {
        return Optional.ofNullable(mapper.selectById(id)).map(this::toDomain);
    }

    @Override
    public List<OrgUnit> findAll() {
        return mapper.selectList(Wrappers.emptyWrapper()).stream().map(this::toDomain).toList();
    }

    @Override
    public List<OrgUnit> findByIds(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return mapper
                .selectList(Wrappers.<SysOrgUnitEntity>lambdaQuery().in(SysOrgUnitEntity::getId, ids))
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public int rebaseSubtreePath(String oldPrefix, String newPrefix, Long selfId) {
        return mapper.rebaseSubtreePath(oldPrefix, newPrefix, selfId);
    }

    @Override
    public void deleteById(Long id) {
        mapper.deleteById(id);
    }

    private OrgUnit toDomain(SysOrgUnitEntity entity) {
        return OrgUnit.restore(
                entity.getId(), entity.getParentId(), entity.getName(), entity.getPath(), entity.getVersion());
    }

    private SysOrgUnitEntity toEntity(OrgUnit unit) {
        SysOrgUnitEntity entity = new SysOrgUnitEntity();
        entity.setId(unit.getId());
        entity.setParentId(unit.getParentId());
        entity.setName(unit.getName());
        entity.setPath(unit.getPath());
        entity.setVersion(unit.getVersion());
        return entity;
    }
}
