package com.fastboot.common;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fastboot.common.error.ErrorCode;
import org.junit.jupiter.api.Test;

class ErrorCodeTest {

  @Test
  void loginErrorIsUnauthorized() {
    assertEquals(401, ErrorCode.USER_OR_PASSWORD.httpStatus());
  }
}
