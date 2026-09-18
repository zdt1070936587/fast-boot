package com.fastboot.common.error;

public class BizException extends RuntimeException {
  private final int code;
  private final int httpStatus;

  public BizException(ErrorCode errorCode) {
    super(errorCode.message());
    this.code = errorCode.code();
    this.httpStatus = errorCode.httpStatus();
  }

  public BizException(ErrorCode errorCode, String message) {
    super(message);
    this.code = errorCode.code();
    this.httpStatus = errorCode.httpStatus();
  }

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
