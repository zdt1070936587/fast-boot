package com.fastboot.modules.system.interfaces;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.fastboot.common.api.R;
import com.fastboot.modules.system.application.PostAppService;
import com.fastboot.modules.system.infrastructure.persistence.entity.SysPostEntity;
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
@RequestMapping("/api/v1/system/posts")
@Tag(name = "system-post")
public class PostController {

    private final PostAppService postAppService;

    public PostController(PostAppService postAppService) {
        this.postAppService = postAppService;
    }

    @GetMapping
    @SaCheckPermission("sys:post:read")
    public R<List<SysPostEntity>> list() {
        return R.ok(postAppService.list());
    }

    @GetMapping("/{id}")
    @SaCheckPermission("sys:post:read")
    public R<SysPostEntity> get(@PathVariable Long id) {
        return R.ok(postAppService.get(id));
    }

    @PostMapping
    @SaCheckPermission("sys:post:write")
    public R<Long> create(@RequestBody SysPostEntity body) {
        return R.ok(postAppService.create(body));
    }

    @PutMapping("/{id}")
    @SaCheckPermission("sys:post:write")
    public R<Void> update(@PathVariable Long id, @RequestBody SysPostEntity body) {
        body.setId(id);
        postAppService.update(body);
        return R.ok();
    }

    @DeleteMapping("/{id}")
    @SaCheckPermission("sys:post:write")
    public R<Void> delete(@PathVariable Long id) {
        postAppService.delete(id);
        return R.ok();
    }
}
