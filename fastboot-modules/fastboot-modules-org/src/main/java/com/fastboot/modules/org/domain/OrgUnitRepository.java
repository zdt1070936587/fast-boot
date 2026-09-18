package com.fastboot.modules.org.domain;

import java.util.List;
import java.util.Optional;

public interface OrgUnitRepository {
  OrgUnit save(OrgUnit unit);

  Optional<OrgUnit> findById(Long id);

  List<OrgUnit> findAll();

  List<OrgUnit> findByPathPrefix(String pathPrefix);

  void deleteById(Long id);
}
