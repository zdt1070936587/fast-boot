package com.fastboot.modules.${packageName}.interfaces;

import com.fastboot.common.api.R;
import com.fastboot.modules.${packageName}.application.${aggregate}AppService;
import com.fastboot.modules.${packageName}.domain.${aggregate};
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/${module}/${aggregateCamel}")
public class ${aggregate}Controller {
    private final ${aggregate}AppService appService;

    public ${aggregate}Controller(${aggregate}AppService appService) {
        this.appService = appService;
    }

    @GetMapping("/{id}")
    public R<${aggregate}> get(@PathVariable Long id) {
        return R.ok(appService.get(id));
    }
}
