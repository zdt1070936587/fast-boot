package com.fastboot.modules.infra.interfaces;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.fastboot.common.api.R;
import com.fastboot.modules.infra.application.NotifyAppService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/infra/notify")
@Tag(name = "infra-notify")
public class NotifyController {

    private final NotifyAppService notifyAppService;

    public NotifyController(NotifyAppService notifyAppService) {
        this.notifyAppService = notifyAppService;
    }

    @PostMapping
    @SaCheckPermission("sys:notify:write")
    public R<Void> send(@RequestBody NotifyRequest request) {
        notifyAppService.send(request.channel(), request.templateCode(), request.receiver(), request.params());
        return R.ok();
    }

    public record NotifyRequest(
            String channel, String templateCode, String receiver, Map<String, Object> params) {}
}
