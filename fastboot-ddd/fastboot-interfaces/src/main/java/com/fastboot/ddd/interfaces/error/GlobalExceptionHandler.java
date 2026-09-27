package com.fastboot.ddd.interfaces.error;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import com.fastboot.common.api.R;
import com.fastboot.common.error.BizException;
import com.fastboot.common.error.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 全局 REST 异常处理，将各类异常统一转换为 {@link R} 响应（支持 i18n）。
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(BizException.class)
    public ResponseEntity<R<Void>> handleBiz(BizException ex) {
        log.warn("biz error code={} msg={}", ex.getCode(), ex.getMessage());
        HttpStatus status = HttpStatus.resolve(ex.getHttpStatus());
        if (status == null) {
            status = HttpStatus.BAD_REQUEST;
        }
        return ResponseEntity.status(status).body(R.fail(ex.getCode(), resolveMessage(ex)));
    }

    @ExceptionHandler(NotLoginException.class)
    public ResponseEntity<R<Void>> handleNotLogin(NotLoginException ex) {
        log.warn("not login: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(R.fail(ErrorCode.UNAUTHORIZED.code(), msg(ErrorCode.UNAUTHORIZED)));
    }

    @ExceptionHandler(NotPermissionException.class)
    public ResponseEntity<R<Void>> handleNotPermission(NotPermissionException ex) {
        log.warn("forbidden: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(R.fail(ErrorCode.FORBIDDEN.code(), msg(ErrorCode.FORBIDDEN)));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<R<Void>> handleValid(MethodArgumentNotValidException ex) {
        String fallback = msg(ErrorCode.VALIDATION_FAILED);
        String detail =
                ex.getBindingResult().getFieldErrors().stream()
                        .findFirst()
                        .map(err -> err.getField() + " " + err.getDefaultMessage())
                        .orElse(fallback);
        log.warn("validation failed: {}", detail);
        return ResponseEntity.badRequest().body(R.fail(ErrorCode.VALIDATION_FAILED.code(), detail));
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

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<R<Void>> handleDataIntegrity(DataIntegrityViolationException ex) {
        log.warn("data integrity: {}", ex.getMostSpecificCause().getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(R.fail(ErrorCode.CONFLICT.code(), msg(ErrorCode.CONFLICT)));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<R<Void>> handle404(NoHandlerFoundException ex) {
        log.warn("not found: {}", ex.getRequestURL());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(R.fail(ErrorCode.NOT_FOUND.code(), msg(ErrorCode.NOT_FOUND)));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<R<Void>> handleAny(Exception ex, HttpServletRequest request) {
        log.error("unhandled {} {}", request.getMethod(), request.getRequestURI(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(R.fail(ErrorCode.INTERNAL_ERROR.code(), msg(ErrorCode.INTERNAL_ERROR)));
    }

    private String resolveMessage(BizException ex) {
        if (ex.getMessage() != null
                && !ex.getMessage().isBlank()
                && !ex.getMessage().equals(defaultEnumMessage(ex.getCode()))) {
            return ex.getMessage();
        }
        for (ErrorCode code : ErrorCode.values()) {
            if (code.code() == ex.getCode()) {
                return msg(code);
            }
        }
        return ex.getMessage();
    }

    private String defaultEnumMessage(int code) {
        for (ErrorCode ec : ErrorCode.values()) {
            if (ec.code() == code) {
                return ec.message();
            }
        }
        return null;
    }

    private String msg(ErrorCode code) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(code.messageKey(), null, code.message(), locale);
    }
}
