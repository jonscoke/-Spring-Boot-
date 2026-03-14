package com.youthhealth.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResultCode {
    SUCCESS(0, "Success"),
    BAD_REQUEST(40000, "Bad request"),
    UNAUTHORIZED(40100, "Unauthorized"),
    FORBIDDEN(40300, "Forbidden"),
    NOT_FOUND(40400, "Not found"),
    VALIDATION_ERROR(40001, "Validation error"),
    INTERNAL_ERROR(50000, "Internal server error");

    private final int code;
    private final String message;
}
