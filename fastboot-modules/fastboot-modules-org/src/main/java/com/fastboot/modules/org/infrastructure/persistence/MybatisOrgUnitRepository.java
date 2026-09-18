package com.fastboot.modules.org.infrastructure.persistence;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fastboot.modules.org.domain.OrgUnit;
import com.fastboot.modules.org.domain.OrgUnitRepository;
import com.fastboot.modules.org.infrastructure.mapper.SysOrgUnitMapper;
import com.fastboot.modules.org.infrastructure.persistence.entity.SysOrgUnitEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class MybatisOrgUnitRepository implements OrgUnitRepository {

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
    } else {
      mapper.updateById(entity);
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
  public List<OrgUnit> findByPathPrefix(String pathPrefix) {
    return mapper
        .selectList(
            Wrappers.<SysOrgUnitEntity>lambdaQuery().likeRight(SysOrgUnitEntity::getPath, pathPrefix))
        .stream()
        .map(this::toDomain)
        .toList();
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
