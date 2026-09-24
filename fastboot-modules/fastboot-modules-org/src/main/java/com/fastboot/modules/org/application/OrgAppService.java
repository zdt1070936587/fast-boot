package com.fastboot.modules.org.application;

import com.fastboot.common.error.BizException;
import com.fastboot.common.error.ErrorCode;
import com.fastboot.common.lock.DistributedLock;
import com.fastboot.modules.authz.api.DataScopePort;
import com.fastboot.modules.org.application.dto.OrgView;
import com.fastboot.modules.org.domain.OrgUnit;
import com.fastboot.modules.org.domain.OrgUnitRepository;
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
    /** 数据范围端口 */
    private final DataScopePort dataScopePort;

    public OrgAppService(OrgUnitRepository orgUnitRepository, DataScopePort dataScopePort) {
        this.orgUnitRepository = orgUnitRepository;
        this.dataScopePort = dataScopePort;
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
        orgUnitRepository.rebaseSubtreePath(oldPath, unit.getPath(), id);
    }

    public List<OrgView> list(Long userId, Long selfOrgId) {
        List<Long> visible = dataScopePort.visibleOrgIds(userId, selfOrgId);
        List<OrgUnit> units =
                visible.isEmpty() ? orgUnitRepository.findAll() : orgUnitRepository.findByIds(visible);
        return units.stream()
                .map(unit -> new OrgView(unit.getId(), unit.getParentId(), unit.getName(), unit.getPath()))
                .toList();
    }
}
