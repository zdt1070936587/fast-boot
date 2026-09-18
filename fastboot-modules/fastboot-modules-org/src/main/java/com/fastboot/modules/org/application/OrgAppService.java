package com.fastboot.modules.org.application;

import com.fastboot.common.error.BizException;
import com.fastboot.common.error.ErrorCode;
import com.fastboot.common.lock.DistributedLock;
import com.fastboot.modules.authz.application.AuthzAppService;
import com.fastboot.modules.org.domain.OrgUnit;
import com.fastboot.modules.org.domain.OrgUnitRepository;
import com.fastboot.modules.org.interfaces.dto.OrgView;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 组织树应用服务
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@Service
public class OrgAppService {

  /** 组织单元仓储 */
  private final OrgUnitRepository orgUnitRepository;
  /** 授权服务（数据范围过滤） */
  private final AuthzAppService authzAppService;

  public OrgAppService(OrgUnitRepository orgUnitRepository, AuthzAppService authzAppService) {
    this.orgUnitRepository = orgUnitRepository;
    this.authzAppService = authzAppService;
  }

  @Transactional
  public Long create(Long parentId, String name) {
    String parentPath = "";
    if (parentId != null) {
      OrgUnit parent =
          orgUnitRepository
              .findById(parentId)
              .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND, "上级组织不存在"));
      parentPath = parent.getPath();
    }
    OrgUnit unit = OrgUnit.create(parentId, name, parentPath);
    orgUnitRepository.save(unit);
    unit.assignIdToPath();
    orgUnitRepository.save(unit);
    return unit.getId();
  }

  @Transactional
  @DistributedLock(key = "'org-move-' + #id")
  public void move(Long id, Long newParentId) {
    OrgUnit unit =
        orgUnitRepository.findById(id).orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND));
    OrgUnit parent =
        orgUnitRepository
            .findById(newParentId)
            .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND, "上级组织不存在"));
    String oldPath = unit.getPath();
    unit.moveTo(parent);
    orgUnitRepository.save(unit);
    for (OrgUnit child : orgUnitRepository.findByPathPrefix(oldPath)) {
      if (child.getId().equals(id)) {
        continue;
      }
        String next = child.getPath().replaceFirst(java.util.regex.Pattern.quote(oldPath), unit.getPath());
      OrgUnit restored =
          OrgUnit.restore(child.getId(), child.getParentId(), child.getName(), next, child.getVersion());
      orgUnitRepository.save(restored);
    }
  }

  public List<OrgView> list(Long userId, Long selfOrgId) {
    List<Long> visible = authzAppService.visibleOrgIds(userId, selfOrgId);
    return orgUnitRepository.findAll().stream()
        .filter(unit -> visible.isEmpty() || visible.contains(unit.getId()))
        .map(unit -> new OrgView(unit.getId(), unit.getParentId(), unit.getName(), unit.getPath()))
        .toList();
  }
}
