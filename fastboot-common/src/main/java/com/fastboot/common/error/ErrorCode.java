package com.fastboot.common.error;

public enum ErrorCode {
  OK(0, 200, "OK"),
  VALIDATION_FAILED(400001, 400, "参数校验失败"),
  NOT_FOUND(404001, 404, "资源不存在"),
  UNAUTHORIZED(401000, 401, "未登录或登录已失效"),
  FORBIDDEN(403000, 403, "无访问权限"),
  USER_OR_PASSWORD(401001, 401, "用户名或密码错误"),
  ACCOUNT_DISABLED(401002, 403, "账号已禁用"),
  PASSWORD_NOT_INITIALIZED(401003, 401, "账号未初始化密码"),
  LOGIN_LOCKED(401004, 429, "登录失败次数过多，请稍后重试"),
  LOGIN_RATE_LIMITED(401005, 429, "登录请求过于频繁"),
  CONFLICT(409001, 409, "数据冲突"),
  IDEMPOTENT_CONFLICT(409002, 409, "重复请求"),
  INTERNAL_ERROR(500000, 500, "系统内部错误");

  private final int code;
  private final int httpStatus;
  private final String message;

  ErrorCode(int code, int httpStatus, String message) {
    this.code = code;
    this.httpStatus = httpStatus;
    this.message = message;
  }

  public int code() {
    return code;
  }

  public int httpStatus() {
    return httpStatus;
  }

  public String message() {
    return message;
  }
}
