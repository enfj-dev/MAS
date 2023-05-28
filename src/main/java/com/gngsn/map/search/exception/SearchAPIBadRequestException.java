package com.gngsn.map.search.exception;

/**
 * 외부 API 검색 HTTP 요청 시 4xx 오류 발생 예외
 */
public class SearchAPIBadRequestException extends RuntimeException {

    public SearchAPIBadRequestException(final String message) {
        super(message);
    }

    public SearchAPIBadRequestException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
