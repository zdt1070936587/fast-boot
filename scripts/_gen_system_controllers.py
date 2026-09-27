import os

BASE = r"E:\code\fast-boot\fastboot-modules\fastboot-modules-system\src\main\java\com\fastboot\modules\system\interfaces"
os.makedirs(BASE, exist_ok=True)

HEADER = """package com.fastboot.modules.system.interfaces;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.fastboot.common.api.R;
"""

controllers = {
    "DictController.java": HEADER
    + """import com.fastboot.modules.system.api.DictQueryPort;
import com.fastboot.modules.system.application.DictTypeAppService;
import com.fastboot.modules.system.infrastructure.persistence.entity.SysDictDataEntity;
import com.fastboot.modules.system.infrastructure.persistence.entity.SysDictTypeEntity;
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
@RequestMapping("/api/v1/system/dicts")
@Tag(name = "system-dict")
public class DictController {

    private final DictQueryPort dictQueryPort;
    private final DictTypeAppService dictTypeAppService;

    public DictController(DictQueryPort dictQueryPort, DictTypeAppService dictTypeAppService) {
        this.dictQueryPort = dictQueryPort;
        this.dictTypeAppService = dictTypeAppService;
    }

    @GetMapping("/types")
    @SaCheckPermission("sys:dict:read")
    public R<List<SysDictTypeEntity>> listTypes() {
        return R.ok(dictTypeAppService.listTypes());
    }

    @PostMapping("/types")
    @SaCheckPermission("sys:dict:write")
    public R<Long> createType(@RequestBody SysDictTypeEntity body) {
        return R.ok(dictTypeAppService.createType(body));
    }

    @PutMapping("/types/{id}")
    @SaCheckPermission("sys:dict:write")
    public R<Void> updateType(@PathVariable Long id, @RequestBody SysDictTypeEntity body) {
        body.setId(id);
        dictTypeAppService.updateType(body);
        return R.ok();
    }

    @GetMapping("/{typeCode}/items")
    @SaCheckPermission("sys:dict:read")
    public R<List<DictQueryPort.DictItem>> items(@PathVariable String typeCode) {
        return R.ok(dictQueryPort.data(typeCode));
    }

    @GetMapping("/{typeCode}/data")
    @SaCheckPermission("sys:dict:read")
    public R<List<SysDictDataEntity>> listData(@PathVariable String typeCode) {
        return R.ok(dictTypeAppService.listData(typeCode));
    }

    @PostMapping("/data")
    @SaCheckPermission("sys:dict:write")
    public R<Long> createData(@RequestBody SysDictDataEntity body) {
        return R.ok(dictTypeAppService.createData(body));
    }

    @PutMapping("/data/{id}")
    @SaCheckPermission("sys:dict:write")
    public R<Void> updateData(@PathVariable Long id, @RequestBody SysDictDataEntity body) {
        body.setId(id);
        dictTypeAppService.updateData(body);
        return R.ok();
    }
}
""",
    "ConfigController.java": HEADER
    + """import com.fastboot.modules.system.application.ConfigAppService;
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
""",
    "TenantController.java": HEADER
    + """import com.fastboot.modules.system.application.TenantAppService;
import com.fastboot.modules.system.infrastructure.persistence.entity.SysTenantEntity;
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
@RequestMapping("/api/v1/system/tenants")
@Tag(name = "system-tenant")
public class TenantController {

    private final TenantAppService tenantAppService;

    public TenantController(TenantAppService tenantAppService) {
        this.tenantAppService = tenantAppService;
    }

    @GetMapping
    @SaCheckPermission("sys:tenant:read")
    public R<List<SysTenantEntity>> list() {
        return R.ok(tenantAppService.list());
    }

    @GetMapping("/{id}")
    @SaCheckPermission("sys:tenant:read")
    public R<com.fastboot.modules.system.api.TenantQueryPort.TenantSnapshot> get(@PathVariable Long id) {
        return R.ok(tenantAppService.find(id).orElse(null));
    }

    @PostMapping
    @SaCheckPermission("sys:tenant:write")
    public R<Long> create(@RequestBody SysTenantEntity body) {
        return R.ok(tenantAppService.create(body));
    }

    @PutMapping("/{id}")
    @SaCheckPermission("sys:tenant:write")
    public R<Void> update(@PathVariable Long id, @RequestBody SysTenantEntity body) {
        body.setId(id);
        tenantAppService.update(body);
        return R.ok();
    }
}
""",
    "TenantPackageController.java": HEADER
    + """import com.fastboot.modules.system.application.TenantPackageAppService;
import com.fastboot.modules.system.infrastructure.persistence.entity.SysTenantPackageEntity;
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
@RequestMapping("/api/v1/system/packages")
@Tag(name = "system-tenant-package")
public class TenantPackageController {

    private final TenantPackageAppService tenantPackageAppService;

    public TenantPackageController(TenantPackageAppService tenantPackageAppService) {
        this.tenantPackageAppService = tenantPackageAppService;
    }

    @GetMapping
    @SaCheckPermission("sys:tenant:read")
    public R<List<SysTenantPackageEntity>> list() {
        return R.ok(tenantPackageAppService.list());
    }

    @GetMapping("/{id}")
    @SaCheckPermission("sys:tenant:read")
    public R<SysTenantPackageEntity> get(@PathVariable Long id) {
        return R.ok(tenantPackageAppService.get(id));
    }

    @PostMapping
    @SaCheckPermission("sys:tenant:write")
    public R<Long> create(@RequestBody SysTenantPackageEntity body) {
        return R.ok(tenantPackageAppService.create(body));
    }

    @PutMapping("/{id}")
    @SaCheckPermission("sys:tenant:write")
    public R<Void> update(@PathVariable Long id, @RequestBody SysTenantPackageEntity body) {
        body.setId(id);
        tenantPackageAppService.update(body);
        return R.ok();
    }
}
""",
    "PostController.java": HEADER
    + """import com.fastboot.modules.system.application.PostAppService;
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
""",
    "NoticeController.java": HEADER
    + """import com.fastboot.modules.system.application.NoticeAppService;
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
""",
    "ErrorCodeController.java": HEADER
    + """import com.fastboot.modules.system.application.ErrorCodeAppService;
import com.fastboot.modules.system.infrastructure.persistence.entity.SysErrorCodeEntity;
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
@RequestMapping("/api/v1/system/error-codes")
@Tag(name = "system-error-code")
public class ErrorCodeController {

    private final ErrorCodeAppService errorCodeAppService;

    public ErrorCodeController(ErrorCodeAppService errorCodeAppService) {
        this.errorCodeAppService = errorCodeAppService;
    }

    @GetMapping
    @SaCheckPermission("sys:error-code:read")
    public R<List<SysErrorCodeEntity>> list() {
        return R.ok(errorCodeAppService.list());
    }

    @GetMapping("/{id}")
    @SaCheckPermission("sys:error-code:read")
    public R<SysErrorCodeEntity> get(@PathVariable Long id) {
        return R.ok(errorCodeAppService.get(id));
    }

    @PostMapping
    @SaCheckPermission("sys:error-code:write")
    public R<Long> create(@RequestBody SysErrorCodeEntity body) {
        return R.ok(errorCodeAppService.create(body));
    }

    @PutMapping("/{id}")
    @SaCheckPermission("sys:error-code:write")
    public R<Void> update(@PathVariable Long id, @RequestBody SysErrorCodeEntity body) {
        body.setId(id);
        errorCodeAppService.update(body);
        return R.ok();
    }

    @DeleteMapping("/{id}")
    @SaCheckPermission("sys:error-code:write")
    public R<Void> delete(@PathVariable Long id) {
        errorCodeAppService.delete(id);
        return R.ok();
    }
}
""",
    "OperationLogController.java": HEADER
    + """import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fastboot.modules.system.application.OperationLogQueryService;
import com.fastboot.modules.system.infrastructure.persistence.entity.SysOperationLogEntity;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/system/operation-logs")
@Tag(name = "system-operation-log")
public class OperationLogController {

    private final OperationLogQueryService operationLogQueryService;

    public OperationLogController(OperationLogQueryService operationLogQueryService) {
        this.operationLogQueryService = operationLogQueryService;
    }

    @GetMapping
    @SaCheckPermission("sys:audit:read")
    public R<Page<SysOperationLogEntity>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return R.ok(operationLogQueryService.page(page, size));
    }
}
""",
    "LoginLogController.java": HEADER
    + """import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fastboot.modules.system.application.LoginLogQueryService;
import com.fastboot.modules.system.infrastructure.persistence.entity.SysLoginLogEntity;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/system/login-logs")
@Tag(name = "system-login-log")
public class LoginLogController {

    private final LoginLogQueryService loginLogQueryService;

    public LoginLogController(LoginLogQueryService loginLogQueryService) {
        this.loginLogQueryService = loginLogQueryService;
    }

    @GetMapping
    @SaCheckPermission("sys:audit:read")
    public R<Page<SysLoginLogEntity>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return R.ok(loginLogQueryService.page(page, size));
    }
}
""",
    "OnlineUserController.java": HEADER
    + """import com.fastboot.modules.system.application.OnlineUserAppService;
import com.fastboot.modules.system.application.dto.OnlineUserView;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/system/online-users")
@Tag(name = "system-online-user")
public class OnlineUserController {

    private final OnlineUserAppService onlineUserAppService;

    public OnlineUserController(OnlineUserAppService onlineUserAppService) {
        this.onlineUserAppService = onlineUserAppService;
    }

    @GetMapping
    @SaCheckPermission("sys:online:read")
    public R<List<OnlineUserView>> list() {
        return R.ok(onlineUserAppService.list());
    }

    @PostMapping("/{userId}/kickout")
    @SaCheckPermission("sys:online:write")
    public R<Void> kickout(@PathVariable Long userId) {
        onlineUserAppService.kickout(userId);
        return R.ok();
    }
}
""",
}

for name, content in controllers.items():
    with open(os.path.join(BASE, name), "w", encoding="utf-8") as f:
        f.write(content)
print("controllers ok")
