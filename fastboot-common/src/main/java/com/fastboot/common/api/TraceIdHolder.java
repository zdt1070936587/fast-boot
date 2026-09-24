package com.fastboot.common.api;

import org.slf4j.MDC;

/**
 * 请求链路追踪 ID 的线程上下文持有者，与 SLF4J MDC 同步。
 *
 * @author zhaodt
 * @since 2026-09-19
 */
public final class TraceIdHolder {
    /** HTTP 请求/响应头中的追踪 ID 字段名 */
    public static final String HEADER = "X-Request-Id";
    /** MDC 中存放 traceId 的键名 */
    public static final String MDC_KEY = "traceId";

    private TraceIdHolder() {}

    /** @return 当前线程的 traceId，未设置时返回 null */
    public static String current() {
        return MDC.get(MDC_KEY);
    }

    /**
     * 设置当前线程 traceId；空值则清除 MDC 中的键。
     *
     * @param traceId 追踪标识
     */
    public static void set(String traceId) {
        if (traceId == null || traceId.isBlank()) {
            MDC.remove(MDC_KEY);
            return;
        }
        MDC.put(MDC_KEY, traceId);
    }

    /** 清除当前线程 MDC 中的 traceId */
    public static void clear() {
        MDC.remove(MDC_KEY);
    }
}
