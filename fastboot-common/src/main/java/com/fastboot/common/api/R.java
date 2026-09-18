package com.fastboot.common.api;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class R<T> {
  private final int code;
  private final String message;
  private final T data;
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

  public static <T> R<T> ok(T data) {
    return new R<>(0, "OK", data, TraceIdHolder.current());
  }

  public static R<Void> ok() {
    return new R<>(0, "OK", null, TraceIdHolder.current());
  }

  public static <T> R<T> fail(int code, String message) {
    return new R<>(code, message, null, TraceIdHolder.current());
  }
}
