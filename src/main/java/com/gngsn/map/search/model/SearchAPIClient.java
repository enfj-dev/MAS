package com.gngsn.map.search.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gngsn.map.search.dto.PlaceSearchResult;
import com.gngsn.map.search.exception.SearchAPIExternalServerException;
import com.gngsn.map.search.exception.SearchAPIBadRequestException;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

import static com.gngsn.map.common.Constants.RETRY_MAX_ATTEMPTS;

/**
 * 외부 Map Search API Client.
 * @param <T> request type per external services.
 */
public abstract class SearchAPIClient<T extends PlaceSearchRequest> {
    public static final ObjectMapper objectMapper = new ObjectMapper();
    protected final WebClient webClient;

    protected SearchAPIClient(final WebClient webClient) {
        this.webClient = webClient;
    }

    /**
     * 각 외부 API 검색 결과 별, Place 객체 리스트로 변환하여 반환.
     */
    public Mono<List<PlaceSearchResult.Place>> getPlaceList(final T params) {
        return getResponse(params).map(PlaceSearchResponse::getPlaceList);
    }

    /**
     * 외부 API 검색 HTTP 요청
     */
    protected abstract Mono<? extends PlaceSearchResponse> getResponse(T params);

    /**
     * 외부 API 검색 HTTP 요청 시, 5xx 오류가 발생할 경우 예외 처리 집중화
     */
    protected Function<ClientResponse, Mono<? extends Throwable>> convert5xxException() {
        return clientResponse ->
            clientResponse.bodyToMono(String.class)
                    .map(response -> new SearchAPIExternalServerException("WebClient External API 호출 시 5xx Server-Side 오류 발생 (Retry). {ClientResponse=" + response + "}"));
    }

    /**
     * 외부 API 검색 HTTP 요청 시, 4xx 오류가 발생할 경우 예외 처리 집중화
     */
    protected Function<ClientResponse, Mono<? extends Throwable>> convert4xxException() {
        return clientResponse ->
                clientResponse.bodyToMono(String.class)
                        .map(response -> new SearchAPIBadRequestException("WebClient External API 호출 시 4xx Client-Side 오류 발생. 데이터 확인 및 조치 필요. {ClientResponse=" + response + "}"));
    }

    /**
     * Backoff 적용한 Webclient Request Retry Strategy.
     * - 5xx 오류 시에만 Retry 시도
     * - maxAttempts = 3: 최초 시도를 포함 3번 재시도 + backoff 지수 2초
     * - jitter: 임의의 난수(계산된 지연의 N%) default 0.5
     * - 모든 Retry 실패 시 WebclientRetryExhaustedException
     */
    protected RetryBackoffSpec buildRetrySpec() {
        return Retry
                .backoff(RETRY_MAX_ATTEMPTS, Duration.ofSeconds(2L))
                .jitter(0.75)
                .filter(throwable -> throwable instanceof SearchAPIExternalServerException);
    }
}
