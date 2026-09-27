package com.fastboot.modules.system.interfaces;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.fastboot.common.api.R;
import com.fastboot.modules.system.application.NoticeAppService;
import com.fastboot.modules.system.infrastructure.persistence.entity.SysNoticeEntity;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/system/notices")
@Tag(name = "system-notice")
public class NoticeController {

    private final NoticeAppService noticeAppService;

    public NoticeController(NoticeAppService noticeAppService) {
        this.noticeAppService = noticeAppService;
    }

    @GetMapping
    @SaCheckPermission("sys:notice:read")
    public R<List<SysNoticeEntity>> list() {
        return R.ok(noticeAppService.list());
    }

    @GetMapping("/{id}")
    @SaCheckPermission("sys:notice:read")
    public R<SysNoticeEntity> get(@PathVariable Long id) {
        return R.ok(noticeAppService.get(id));
    }

    @PostMapping
    @SaCheckPermission("sys:notice:write")
    public R<Long> create(@RequestBody SysNoticeEntity body) {
        return R.ok(noticeAppService.create(body));
    }

    @PutMapping("/{id}")
    @SaCheckPermission("sys:notice:write")
    public R<Void> update(@PathVariable Long id, @RequestBody SysNoticeEntity body) {
        body.setId(id);
        noticeAppService.update(body);
        return R.ok();
    }

    @DeleteMapping("/{id}")
    @SaCheckPermission("sys:notice:write")
    public R<Void> delete(@PathVariable Long id) {
        noticeAppService.delete(id);
        return R.ok();
    }
}
