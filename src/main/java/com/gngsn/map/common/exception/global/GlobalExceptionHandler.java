package com.gngsn.map.common.exception.global;

import com.gngsn.map.common.dto.ErrorResponse;
import com.gngsn.map.search.exception.SearchAPIBadRequestException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.util.stream.StreamSupport;

import static com.gngsn.map.common.exception.global.GlobalExceptionHandler.ConstantResponse.SERVICE_UNAVAILABLE;

/**
 * Exception 발생시 후 처리가 필요한 내용 핸들러
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Default Exception Handling
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public Mono<ResponseEntity<ErrorResponse>> rootException(final Exception e) {
        log.error("RootException={Error={}}", e.getMessage());
        e.printStackTrace();
        return SERVICE_UNAVAILABLE;
    }

    /**
     * 외부 API 검색 HTTP 요청 시 4xx 오류 발생 예외 시 사용자에게 서비스 일시 오류 명시
     */
    @ExceptionHandler(SearchAPIBadRequestException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public Mono<ResponseEntity<ErrorResponse>> searchAPIBadReqeustException(final SearchAPIBadRequestException e) {
        return SERVICE_UNAVAILABLE;
    }

    /**
     * 사용자의 입력 값이 유효하지 않은 경우
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> constraintViolationException(final ConstraintViolationException e) {
        return ResponseEntity.badRequest().body(
                new ErrorResponse(buildValidationErrorMessage(e.getConstraintViolations().iterator().next())));
    }

    private String buildValidationErrorMessage(final ConstraintViolation<?> violation) {
        return String.format("[%s] %s.", StreamSupport
                        .stream(violation.getPropertyPath().spliterator(), false)
                        .reduce((first, second) -> second).orElse(null)
                                        , violation.getMessage());
    }

    static class ConstantResponse {
        public static final Mono<ResponseEntity<ErrorResponse>> INVALID_PARAMETER =
                Mono.just(ResponseEntity.badRequest().body(new ErrorResponse(
                        HttpStatus.BAD_REQUEST.getReasonPhrase())));

        public static final Mono<ResponseEntity<ErrorResponse>> SERVICE_UNAVAILABLE =
        Mono.just(ResponseEntity.internalServerError()
                .body(new ErrorResponse("서비스에 일시적인 문제가 있습니다. 잠시 후 다시 시도해 보세요.")));
    }
}
