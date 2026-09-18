package com.fastboot.common.api;

import java.util.List;

public record PageData<T>(List<T> items, long total, long page, long size) {

  public static <T> PageData<T> of(List<T> items, long total, long page, long size) {
    return new PageData<>(items, total, page, size);
  }
}
