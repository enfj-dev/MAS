package com.gngsn.map.common.exception.global;

import com.gngsn.map.common.dto.ErrorResponse;
import com.gngsn.map.search.exception.SearchAPIBadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

import static com.gngsn.map.common.exception.global.GlobalExceptionHandler.ConstantResponse.INVALID_PARAMETER;

/**
 * Exception 발생시 후 처리가 필요한 내용 핸들러
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    final private Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Default Exception Handling
     */
    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponse>> rootException(final Exception e) {
        log.error("RootException{error={}}", e.getMessage(), e);
        return INVALID_PARAMETER;
    }

    /**
     * 외부 API 검색 HTTP 요청 시 4xx 오류 발생 예외 시 사용자에게 서비스 일시 오류 명시
     */
    @ExceptionHandler(SearchAPIBadRequestException.class)
    public Mono<ResponseEntity<ErrorResponse>> searchAPIBadReqeustException(final SearchAPIBadRequestException e) {
        return Mono.just(ResponseEntity.badRequest()
                .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.name(),
                        "서비스에 일시적인 문제가 있습니다. 잠시 후 다시 시도해 보세요.")));
    }

    /**
     * WebClient 요청 오류 시 사용자에게 서비스 일시 오류 명시
     */
    @ExceptionHandler(WebClientRequestException.class)
    public Mono<ResponseEntity<ErrorResponse>> WebClientRequestException(final WebClientRequestException e) {
        return Mono.just(ResponseEntity.badRequest()
                .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.name(),
                        "서비스에 일시적인 문제가 있습니다. 잠시 후 다시 시도해 보세요.")));
    }

    static class ConstantResponse {
        public static final Mono<ResponseEntity<ErrorResponse>> INVALID_PARAMETER =
                Mono.just(ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), HttpStatus.BAD_REQUEST.getReasonPhrase())));
    }
}
