package com.fastboot.modules.infra.interfaces;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.fastboot.common.api.R;
import com.fastboot.modules.infra.application.JobAppService;
import com.fastboot.modules.infra.infrastructure.persistence.entity.SysJobEntity;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/infra/jobs")
@Tag(name = "infra-job")
public class JobController {

    private final JobAppService jobAppService;

    public JobController(JobAppService jobAppService) {
        this.jobAppService = jobAppService;
    }

    @GetMapping
    @SaCheckPermission("sys:job:read")
    public R<List<SysJobEntity>> list() {
        return R.ok(jobAppService.list());
    }

    @PostMapping
    @SaCheckPermission("sys:job:write")
    public R<Long> create(@RequestBody SysJobEntity body) {
        return R.ok(jobAppService.create(body));
    }

    @PutMapping("/{id}")
    @SaCheckPermission("sys:job:write")
    public R<Void> update(@PathVariable Long id, @RequestBody SysJobEntity body) {
        body.setId(id);
        jobAppService.update(body);
        return R.ok();
    }

    @PostMapping("/{id}/trigger")
    @SaCheckPermission("sys:job:write")
    public R<Void> trigger(@PathVariable Long id) {
        jobAppService.trigger(id);
        return R.ok();
    }

    @PostMapping("/{id}/pause")
    @SaCheckPermission("sys:job:write")
    public R<Void> pause(@PathVariable Long id) {
        jobAppService.pause(id);
        return R.ok();
    }

    @PostMapping("/{id}/resume")
    @SaCheckPermission("sys:job:write")
    public R<Void> resume(@PathVariable Long id) {
        jobAppService.resume(id);
        return R.ok();
    }
}
