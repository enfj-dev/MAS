package com.gngsn.map.search.exception;

/**
 * 외부 API 검색 HTTP 요청 시 5xx 오류 발생 예외
 */
public class SearchAPIExternalServerException extends IllegalStateException {

    public SearchAPIExternalServerException(final String message) {
        super(message);
    }

    public SearchAPIExternalServerException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
