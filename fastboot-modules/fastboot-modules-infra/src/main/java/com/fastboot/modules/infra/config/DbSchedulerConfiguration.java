package com.fastboot.modules.infra.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * db-scheduler 集成占位：启用 {@code fastboot.infra.scheduler.enabled=true} 后由
 * {@code db-scheduler-spring-boot-starter} 使用 {@code scheduled_tasks} 表调度；
 * 任务定义仍来自 {@code sys_job}，可通过 {@link com.fastboot.modules.infra.application.JobAppService} 触发。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@Configuration
@ConditionalOnProperty(prefix = "fastboot.infra.scheduler", name = "enabled", havingValue = "true")
public class DbSchedulerConfiguration {}
