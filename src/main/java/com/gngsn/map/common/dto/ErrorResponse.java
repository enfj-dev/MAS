package com.gngsn.map.common.dto;

/**
 * 오류 발생 시 반환 객체
 */
public class ErrorResponse {
    private String errorType;
    private String message;

    public ErrorResponse() {
    }

    public ErrorResponse(final String errorType, final String message) {
        this.errorType = errorType;
        this.message = message;
    }
}
