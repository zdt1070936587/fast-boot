package com.fastboot.modules.system.interfaces;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.fastboot.common.api.R;
import com.fastboot.modules.system.application.ConfigAppService;
import com.fastboot.modules.system.infrastructure.persistence.entity.SysConfigEntity;
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
@RequestMapping("/api/v1/system/configs")
@Tag(name = "system-config")
public class ConfigController {

    private final ConfigAppService configAppService;

    public ConfigController(ConfigAppService configAppService) {
        this.configAppService = configAppService;
    }

    @GetMapping
    @SaCheckPermission("sys:config:read")
    public R<List<SysConfigEntity>> list() {
        return R.ok(configAppService.listAll());
    }

    @PostMapping
    @SaCheckPermission("sys:config:write")
    public R<Long> create(@RequestBody SysConfigEntity body) {
        return R.ok(configAppService.create(body));
    }

    @PutMapping("/{id}")
    @SaCheckPermission("sys:config:write")
    public R<Void> update(@PathVariable Long id, @RequestBody SysConfigEntity body) {
        body.setId(id);
        configAppService.update(body);
        return R.ok();
    }
}
