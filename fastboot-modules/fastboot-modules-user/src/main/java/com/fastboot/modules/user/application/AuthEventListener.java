package com.fastboot.modules.user.application;

import com.fastboot.modules.user.domain.event.UserLoggedIn;
import com.fastboot.modules.user.domain.event.UserLoggedOut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 认证领域事件监听：闭环消费登录/登出事件。
 *
 * @author zhaodt
 * @since 2026-09-24
 */
@Component
public class AuthEventListener {

    private static final Logger log = LoggerFactory.getLogger(AuthEventListener.class);

    @EventListener
    public void onLoggedIn(UserLoggedIn event) {
        log.info("audit event=UserLoggedIn userId={} username={}", event.userId(), event.username());
    }

    @EventListener
    public void onLoggedOut(UserLoggedOut event) {
        log.info("audit event=UserLoggedOut userId={}", event.userId());
    }
}
