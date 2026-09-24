package com.fastboot.common.api;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 统一 API 响应体。
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class R<T> {
    /** 业务状态码，0 表示成功 */
    private final int code;
    /** 提示信息 */
    private final String message;
    /** 业务数据载荷 */
    private final T data;
    /** 当前请求的链路追踪 ID */
    private final String traceId;

    public R(int code, String message, T data, String traceId) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.traceId = traceId;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public String getTraceId() {
        return traceId;
    }

    /** @param data 成功时的数据 */
    public static <T> R<T> ok(T data) {
        return new R<>(0, "OK", data, TraceIdHolder.current());
    }

    /** @return 无数据的成功响应 */
    public static R<Void> ok() {
        return new R<>(0, "OK", null, TraceIdHolder.current());
    }

    /**
     * @param code 错误码
     * @param message 错误说明
     */
    public static <T> R<T> fail(int code, String message) {
        return new R<>(code, message, null, TraceIdHolder.current());
    }
}
