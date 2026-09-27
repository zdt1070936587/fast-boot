package com.fastboot.modules.system.application;

import cn.dev33.satoken.stp.StpUtil;
import com.fastboot.modules.system.application.dto.OnlineUserView;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 在线用户会话管理。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@Service
public class OnlineUserAppService {

    public List<OnlineUserView> list() {
        List<String> tokens = StpUtil.searchTokenValue("", 0, -1, false);
        List<OnlineUserView> views = new ArrayList<>();
        for (String token : tokens) {
            Object loginId = StpUtil.getLoginIdByToken(token);
            if (!(loginId instanceof Number number)) {
                continue;
            }
            Long userId = number.longValue();
            Object tenantRaw = StpUtil.getSessionByLoginId(userId).get("tenantId");
            Long tenantId = tenantRaw instanceof Number n ? n.longValue() : null;
            views.add(new OnlineUserView(userId, tenantId, token));
        }
        return views;
    }

    public void kickout(Long userId) {
        StpUtil.kickout(userId);
    }
}
