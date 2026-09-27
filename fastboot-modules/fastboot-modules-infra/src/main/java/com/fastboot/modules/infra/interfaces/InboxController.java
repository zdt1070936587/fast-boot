package com.fastboot.modules.infra.interfaces;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.fastboot.common.api.R;
import com.fastboot.modules.infra.application.InboxAppService;
import com.fastboot.modules.infra.infrastructure.persistence.entity.SysInboxEntity;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/infra/inbox")
@Tag(name = "infra-inbox")
public class InboxController {

    private final InboxAppService inboxAppService;

    public InboxController(InboxAppService inboxAppService) {
        this.inboxAppService = inboxAppService;
    }

    @GetMapping
    @SaCheckPermission("sys:inbox:read")
    public R<List<SysInboxEntity>> list() {
        return R.ok(inboxAppService.listForUser(StpUtil.getLoginIdAsLong()));
    }

    @PostMapping("/{id}/read")
    @SaCheckPermission("sys:inbox:write")
    public R<Void> markRead(@PathVariable Long id) {
        inboxAppService.markRead(id);
        return R.ok();
    }
}
