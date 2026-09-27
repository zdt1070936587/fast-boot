package com.fastboot.modules.org.interfaces;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.fastboot.common.api.R;
import com.fastboot.modules.org.application.OrgAppService;
import com.fastboot.modules.org.application.dto.OrgView;
import com.fastboot.modules.org.interfaces.dto.OrgCreateRequest;
import com.fastboot.modules.org.interfaces.dto.OrgMoveRequest;
import com.fastboot.modules.org.interfaces.dto.OrgUpdateRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 组织树管理 REST 接口
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@RestController
@RequestMapping("/api/v1/org")
@Tag(name = "org")
public class OrgController {

    /** 组织树应用服务 */
    private final OrgAppService orgAppService;

    public OrgController(OrgAppService orgAppService) {
        this.orgAppService = orgAppService;
    }

    @GetMapping
    @SaCheckPermission("sys:org:read")
    public R<List<OrgView>> list() {
        Long selfOrgId = null;
        Object raw = StpUtil.getSession().get("orgUnitId");
        if (raw instanceof Number number) {
            selfOrgId = number.longValue();
        }
        return R.ok(orgAppService.list(StpUtil.getLoginIdAsLong(), selfOrgId));
    }

    @PostMapping
    @SaCheckPermission("sys:org:write")
    public R<Long> create(@Valid @RequestBody OrgCreateRequest request) {
        return R.ok(orgAppService.create(request.parentId(), request.name()));
    }

    @PutMapping("/{id}/move")
    @SaCheckPermission("sys:org:write")
    public R<Void> move(@PathVariable Long id, @Valid @RequestBody OrgMoveRequest request) {
        orgAppService.move(id, request.parentId());
        return R.ok();
    }

    @PutMapping("/{id}")
    @SaCheckPermission("sys:org:write")
    public R<Void> update(@PathVariable Long id, @Valid @RequestBody OrgUpdateRequest request) {
        orgAppService.update(id, request.name());
        return R.ok();
    }

    @DeleteMapping("/{id}")
    @SaCheckPermission("sys:org:write")
    public R<Void> delete(@PathVariable Long id) {
        orgAppService.delete(id);
        return R.ok();
    }
}
