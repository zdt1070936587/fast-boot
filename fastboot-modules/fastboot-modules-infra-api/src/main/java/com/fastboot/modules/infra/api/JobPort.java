package com.fastboot.modules.infra.api;

/**
 * 定时任务调度端口。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
public interface JobPort {

    void register(Long jobId);

    void trigger(Long jobId);

    void pause(Long jobId);

    void resume(Long jobId);
}
