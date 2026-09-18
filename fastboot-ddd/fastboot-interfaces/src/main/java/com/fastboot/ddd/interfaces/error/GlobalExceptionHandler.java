package com.fastboot.ddd.interfaces.error;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import com.fastboot.common.api.R;
import com.fastboot.common.error.BizException;
import com.fastboot.common.error.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(BizException.class)
  public ResponseEntity<R<Void>> handleBiz(BizException ex) {
    log.warn("biz error code={} msg={}", ex.getCode(), ex.getMessage());
    HttpStatus status = HttpStatus.resolve(ex.getHttpStatus());
    if (status == null) {
      status = HttpStatus.OK;
    }
    return ResponseEntity.status(status).body(R.fail(ex.getCode(), ex.getMessage()));
  }

  @ExceptionHandler(NotLoginException.class)
  public ResponseEntity<R<Void>> handleNotLogin(NotLoginException ex) {
    log.warn("not login: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(R.fail(ErrorCode.UNAUTHORIZED.code(), ErrorCode.UNAUTHORIZED.message()));
  }

  @ExceptionHandler(NotPermissionException.class)
  public ResponseEntity<R<Void>> handleNotPermission(NotPermissionException ex) {
    log.warn("forbidden: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(R.fail(ErrorCode.FORBIDDEN.code(), ErrorCode.FORBIDDEN.message()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<R<Void>> handleValid(MethodArgumentNotValidException ex) {
    String msg =
        ex.getBindingResult().getFieldErrors().stream()
            .findFirst()
            .map(err -> err.getField() + " " + err.getDefaultMessage())
            .orElse(ErrorCode.VALIDATION_FAILED.message());
    log.warn("validation failed: {}", msg);
    return ResponseEntity.badRequest().body(R.fail(ErrorCode.VALIDATION_FAILED.code(), msg));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<R<Void>> handleConstraint(ConstraintViolationException ex) {
    log.warn("constraint: {}", ex.getMessage());
    return ResponseEntity.badRequest()
        .body(R.fail(ErrorCode.VALIDATION_FAILED.code(), ex.getMessage()));
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<R<Void>> handleUnreadable(HttpMessageNotReadableException ex) {
    log.warn("bad request body: {}", ex.getMessage());
    return ResponseEntity.badRequest()
        .body(R.fail(ErrorCode.VALIDATION_FAILED.code(), "请求体无法解析"));
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<R<Void>> handle404(NoHandlerFoundException ex) {
    log.warn("not found: {}", ex.getRequestURL());
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(R.fail(ErrorCode.NOT_FOUND.code(), ErrorCode.NOT_FOUND.message()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<R<Void>> handleAny(Exception ex, HttpServletRequest request) {
    log.error("unhandled {} {}", request.getMethod(), request.getRequestURI(), ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(R.fail(ErrorCode.INTERNAL_ERROR.code(), ErrorCode.INTERNAL_ERROR.message()));
  }
}
