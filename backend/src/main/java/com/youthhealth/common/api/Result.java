package com.youthhealth.common.api;

import com.youthhealth.common.enums.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private int code;
    private String message;
    private T data;
    private String traceId;
    private LocalDateTime timestamp;

    public static <T> Result<T> success(T data) {
        return Result.<T>builder()
                .code(ResultCode.SUCCESS.getCode())
                .message(ResultCode.SUCCESS.getMessage())
                .data(data)
                .traceId(UUID.randomUUID().toString())
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> failure(ResultCode resultCode) {
        return failure(resultCode, null);
    }

    public static <T> Result<T> failure(ResultCode resultCode, T data) {
        return Result.<T>builder()
                .code(resultCode.getCode())
                .message(resultCode.getMessage())
                .data(data)
                .traceId(UUID.randomUUID().toString())
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> Result<T> failure(int code, String message) {
        return Result.<T>builder()
                .code(code)
                .message(message)
                .data(null)
                .traceId(UUID.randomUUID().toString())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
