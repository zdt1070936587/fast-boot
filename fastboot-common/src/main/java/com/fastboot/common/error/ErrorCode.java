package com.fastboot.common.error;

/**
 * 统一业务错误码，映射 HTTP 状态与对外提示文案。
 *
 * @author zhaodt
 * @since 2026-09-19
 */
public enum ErrorCode {
    /** 成功 */
    OK(0, 200, "OK"),
    /** 请求参数校验失败 */
    VALIDATION_FAILED(400001, 400, "参数校验失败"),
    /** 资源不存在 */
    NOT_FOUND(404001, 404, "资源不存在"),
    /** 未登录或会话失效 */
    UNAUTHORIZED(401000, 401, "未登录或登录已失效"),
    /** 无访问权限 */
    FORBIDDEN(403000, 403, "无访问权限"),
    /** 用户名或密码错误 */
    USER_OR_PASSWORD(401001, 401, "用户名或密码错误"),
    /** 账号已禁用 */
    ACCOUNT_DISABLED(401002, 403, "账号已禁用"),
    /** 账号尚未初始化密码 */
    PASSWORD_NOT_INITIALIZED(401003, 401, "账号未初始化密码"),
    /** 登录失败次数过多被锁定 */
    LOGIN_LOCKED(401004, 429, "登录失败次数过多，请稍后重试"),
    /** 登录请求频率超限 */
    LOGIN_RATE_LIMITED(401005, 429, "登录请求过于频繁"),
    /** 业务数据冲突 */
    CONFLICT(409001, 409, "数据冲突"),
    /** 幂等重复请求 */
    IDEMPOTENT_CONFLICT(409002, 409, "重复请求"),
    /** 未预期的系统内部错误 */
    INTERNAL_ERROR(500000, 500, "系统内部错误");

    /** 业务错误码 */
    private final int code;
    /** 建议的 HTTP 状态码 */
    private final int httpStatus;
    /** 默认错误提示信息 */
    private final String message;

    ErrorCode(int code, int httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    /** @return 业务错误码 */
    public int code() {
        return code;
    }

    /** @return HTTP 状态码 */
    public int httpStatus() {
        return httpStatus;
    }

    /** @return 默认提示文案 */
    public String message() {
        return message;
    }
}
