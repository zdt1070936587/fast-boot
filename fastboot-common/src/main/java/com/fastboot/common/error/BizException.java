package com.fastboot.common.error;

/**
 * 可预期的业务异常，携带业务码与 HTTP 状态映射。
 *
 * @author zhaodt
 * @since 2026-09-19
 */
public class BizException extends RuntimeException {
  /** 业务错误码 */
  private final int code;
  /** 建议返回的 HTTP 状态码 */
  private final int httpStatus;

  /** @param errorCode 预定义错误码 */
  public BizException(ErrorCode errorCode) {
    super(errorCode.message());
    this.code = errorCode.code();
    this.httpStatus = errorCode.httpStatus();
  }

  /**
   * @param errorCode 错误码枚举（决定 code 与 httpStatus）
   * @param message 自定义提示信息
   */
  public BizException(ErrorCode errorCode, String message) {
    super(message);
    this.code = errorCode.code();
    this.httpStatus = errorCode.httpStatus();
  }

  /**
   * @param code 业务错误码
   * @param message 提示信息
   */
  public BizException(int code, String message) {
    super(message);
    this.code = code;
    this.httpStatus = 200;
  }

  public int getCode() {
    return code;
  }

  public int getHttpStatus() {
    return httpStatus;
  }
}
