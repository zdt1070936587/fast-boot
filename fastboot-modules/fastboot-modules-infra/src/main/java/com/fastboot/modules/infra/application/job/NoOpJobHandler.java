package com.fastboot.modules.infra.application.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 默认空任务处理器。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@Component
public class NoOpJobHandler implements JobHandler {

    private static final Logger log = LoggerFactory.getLogger(NoOpJobHandler.class);

    @Override
    public String name() {
        return "noop";
    }

    @Override
    public void execute(String params) {
        log.info("noop job executed params={}", params);
    }
}
