package com.fastboot.modules.system.interfaces;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.fastboot.common.api.R;
import com.fastboot.modules.system.application.OnlineUserAppService;
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
