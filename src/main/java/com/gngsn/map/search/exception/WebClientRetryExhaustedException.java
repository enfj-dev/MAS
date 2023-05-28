package com.gngsn.map.search.exception;

/**
 * 외부 요청 시 Retry 실패 오류
 */
public class WebClientRetryExhaustedException extends RuntimeException {

    public WebClientRetryExhaustedException(final String message) {
        super(message);
    }

    public WebClientRetryExhaustedException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
