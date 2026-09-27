package com.fastboot.modules.system.interfaces;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.fastboot.common.api.R;
import com.fastboot.modules.system.api.DictQueryPort;
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
