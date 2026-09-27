package com.fastboot.modules.infra.application.job;

/**
 * 定时任务处理器 SPI。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
public interface JobHandler {

    /** 与 sys_job.handler 对应 */
    String name();

    void execute(String params);
}
