package com.fastboot.common.api;

import org.slf4j.MDC;

public final class TraceIdHolder {
  public static final String HEADER = "X-Request-Id";
  public static final String MDC_KEY = "traceId";

  private TraceIdHolder() {}

  public static String current() {
    return MDC.get(MDC_KEY);
  }

  public static void set(String traceId) {
    if (traceId == null || traceId.isBlank()) {
      MDC.remove(MDC_KEY);
      return;
    }
    MDC.put(MDC_KEY, traceId);
  }

  public static void clear() {
    MDC.remove(MDC_KEY);
  }
}
